import React, { useState, useEffect, useRef } from "react";
import { Box, Autocomplete, Button, TextField, FormControl, Popover, List, ListItem, ListItemText } from "@mui/material";
import { DateRangePicker, createStaticRanges, defaultStaticRanges } from "react-date-range";
import { format, startOfDay, endOfDay, addDays, isSameDay, addMonths, differenceInCalendarDays } from 'date-fns';
import 'react-date-range/dist/styles.css';
import 'react-date-range/dist/theme/default.css';
import './../styles/react-date-range-custom.css';
import { request } from "../helpers/axios_helper";
import { Link, useNavigate  } from "react-router-dom"
  
const EventSearchBar = ({ searchParams }) => {
    const navigate = useNavigate();
    const [searchText, setSearchText] = useState("");
    const [selectedCategory, setSelectedCategory] = useState("");
    const [selectedLocation, setSelectedLocation] = useState("");
    // const [dateRange, setDateRange] = useState({
    //     startDate: new Date(),
    //     endDate: new Date(),
    //     key: "selection",
    // });
    const [dateRange, setDateRange] = useState(null);


    const [anchorEl, setAnchorEl] = useState(null);
    const [locations, setLocations] = useState([]);
    const [openPopover2, setOpenPopover2] = useState(false);
    const [filteredEvents, setFilteredEvents] = useState([]);
    const [categories, setCategories] = useState([]);
    const textFieldRef = useRef(null);

    const customStaticRanges = createStaticRanges([
        {
            label: 'Today',
            range: () => ({
                startDate: startOfDay(new Date()),
                endDate: endOfDay(new Date()),
            }),
        },
        {
            label: 'Tomorrow',
            range: () => ({
                startDate: startOfDay(addDays(new Date(), 1)),
                endDate: endOfDay(addDays(new Date(), 1)),
            }),
        },
        {
            label: 'Next Week (Mon-Sun)',
            range: () => {
                const now = new Date();
                const dayOfWeek = now.getDay();
                const startOfNextWeek = addDays(now, 8 - dayOfWeek);
                const endOfNextWeek = addDays(startOfNextWeek, 6);
                return {
                    startDate: startOfDay(startOfNextWeek),
                    endDate: endOfDay(endOfNextWeek),
                };
            },
        },
        {
            label: 'Next Month',
            range: () => {
                const now = new Date();
                const startOfNextMonth = startOfDay(addMonths(new Date(now.getFullYear(), now.getMonth() + 1, 1), 0));
                const endOfNextMonth = endOfDay(addMonths(new Date(now.getFullYear(), now.getMonth() + 2, 0), 0));
                return {
                    startDate: startOfNextMonth,
                    endDate: endOfNextMonth,
                };
            },
        },
        {
            label: 'Next 30 Days',
            range: () => ({
                startDate: new Date(),
                endDate: addDays(new Date(), 30),
            }),
        },
        {
            label: 'Next 90 Days',
            range: () => ({
                startDate: new Date(),
                endDate: addDays(new Date(), 90),
            }),
        },
    ]);

    const customInputRanges = [
        {
            label: 'days starting today',
            range: (value) => ({
                startDate: new Date(),
                endDate: addDays(new Date(), Math.max(Number(value), 1) - 1),
            }),
            getCurrentValue(range) {
                if (!isSameDay(range.startDate, startOfDay(new Date()))) return '-';
                if (!range.endDate) return '∞';
                return differenceInCalendarDays(range.endDate, startOfDay(new Date())) + 1;
            },
        },
    ];


    useEffect(() => {
        fetchCategories();
        fetchLocations();

        if (searchParams !== undefined) {
            const filters = Object.fromEntries(searchParams.entries());
            console.log(filters.category, filters.cityName);

            setSearchText(filters.searchText || "");
            setSelectedCategory(filters.category && filters.category !== "null" ? filters.category : "");
            setSelectedLocation(filters.cityName && filters.cityName !== "null" ? filters.cityName : "");
            setDateRange(
                filters.dateStart && filters.dateEnd
                    ? {
                        startDate: new Date(filters.dateStart),
                        endDate: new Date(filters.dateEnd),
                        key: "selection"
                    }
                    : null
            );
        }
    }, []);

    const handleDateClick = (event) => {
        if (!dateRange) {
            setDateRange({
                startDate: new Date(),
                endDate: new Date(),
                key: "selection",
            });
        }
        setAnchorEl(event.currentTarget);
    };

    const fetchLocations = () => {
        request("GET", "/api/dicts/location_cities")
            .then((response) => {
                setLocations(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania lokalizacji:", error);
            });
    }

    const fetchCategories = () => {
        request("GET", "/api/dicts/event_categories")
            .then((response) => {
                setCategories(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania kategorii:", error);
            });
    }

    const searchEvents = (text) => {
        if (text.trim() !== "") {
            request("GET", `/api/events/search?searchText=${text}&page=1&size=10`)
                .then((response) => {
                    console.log("Pobrano eventy:", response.data.events, text);
                    setFilteredEvents(response.data.events || []);
                })
                .catch((error) => {
                    console.error("Błąd podczas pobierania eventów:", error);
                });
        }
        else {
            setFilteredEvents([]);
        }
    };

    const handleSearchTextChange = (event) => {
        const text = event.target.value;
        if (text.trim() !== "") {
            setOpenPopover2(true);
        }
        else {
            setOpenPopover2(false);
        }

        setSearchText(text);
        searchEvents(text);
    };

    const handleCategoryChange = (event, newValue) => {
      setSelectedCategory(newValue);
    };
  
    const handleLocationChange = (event, newValue) => {
      setSelectedLocation(newValue);
    };
  

    const handleSearch = () => {
        const newSearchParams = new URLSearchParams({});

        if (searchText) {
            newSearchParams.set("searchText", searchText);
        }
        if (selectedCategory) {
            newSearchParams.set("category", selectedCategory);
        }
        if (selectedLocation) {
            newSearchParams.set("cityName", selectedLocation);
        }

        console.log("dateRange", dateRange);
        if (dateRange) {
            newSearchParams.set("dateStart", format(startOfDay(dateRange.startDate), "yyyy-MM-dd'T'HH:mm:ss"));
            newSearchParams.set("dateEnd", format(endOfDay(dateRange.endDate), "yyyy-MM-dd'T'HH:mm:ss"));
        }

        window.location.href = `/events?${newSearchParams.toString()}`;

        
        // request("GET", `/api/events?category=${selectedCategory}&cityName=${selectedLocation}&dateStart=${formattedStartDate}&dateEnd=${formattedEndDate}&searchText=${searchText}&page=1&size=10`)
        //     .then((response) => {
        //         console.log("Pobrano eventy:", response.data);
        //     })
        //     .catch((error) => {
        //         console.error("Błąd podczas pobierania eventów:", error);
        //     });
    };

    const handlePopoverClose = () => {
        setOpenPopover2(false);
    };

    const openPopover = Boolean(anchorEl);
    const id = openPopover ? "date-popover" : undefined;

    return (
        <div style={{ padding: "20px", backgroundColor: "#f4f4f4", marginBottom: "32px" }}>
        <Box display="flex" alignItems="center" justifyContent="space-between" marginBottom="20px">
            <TextField
                ref={textFieldRef}
                label="Search Events"
                fullWidth
                variant="outlined"
                margin="normal"
                value={searchText}
                onChange={handleSearchTextChange}
                style={{ flex: 1, marginRight: "10px", marginTop: "0px", marginBottom: "0px" }}
            />

            <Button
                variant="contained"
                color="primary"
                onClick={handleSearch}
                style={{ width: "120px", height: "56px", marginRight: "8px" }}
            >
                Search
            </Button>

            {/* Popover for event search */}
            <Popover
                open={openPopover2}
                anchorEl={textFieldRef.current}
                onClose={handlePopoverClose}
                disableAutoFocus
                disableEnforceFocus
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'left',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'left',
                }}
            >
                <List>
                    {filteredEvents.length === 0 ? (
                        <ListItem>
                            <ListItemText primary="No events found" />
                        </ListItem>
                    ) : (
                        filteredEvents.map((event) => (
                            <ListItem
                                key={event.id}
                                width="100%"
                            >
                                <ListItemText
                                    primary={
                                        <Link
                                            to={`/events/${event.id}`}
                                            style={{
                                                textDecoration: "none",
                                                color: "inherit",
                                            }}
                                        >
                                            {event.name}
                                        </Link>
                                    }
                                />
                            </ListItem>
                        ))
                    )}
                </List>
            </Popover>
        </Box>
  
        <Box display="flex" alignItems="center" justifyContent="space-between" marginTop="10px">
          {/* Date Range Selection */}
          <TextField
            label="Date Range"
            fullWidth
            variant="outlined"
            value={
                dateRange
                    ? `${format(dateRange.startDate, "dd.MM.yyyy")} - ${format(dateRange.endDate, "dd.MM.yyyy")}`
                    : ""
            }
            onClick={(event) => handleDateClick(event)}
            readOnly
            style={{ flex: 1, marginRight: "10px" }}
          />
          
          <FormControl variant="outlined" style={{ flex: 1, marginRight: "10px" }}>
            <Autocomplete
                value={selectedCategory}
                onChange={handleCategoryChange}
                options={categories.map(category => category.name)}
                renderInput={(params) => (
                    <TextField {...params} label="Category" variant="outlined" />
                )}
                freeSolo
            />
          </FormControl>
  
          <FormControl variant="outlined" style={{ flex: 1, marginRight: "10px" }}>
            <Autocomplete
                value={selectedLocation}
                onChange={handleLocationChange}
                options={locations.map(location => location.name)} 
                renderInput={(params) => (
                    <TextField {...params} label="Location" variant="outlined" />
                )}
                freeSolo
            />
          </FormControl>
        </Box>
  
        {/* Popover for Date Range Picker */}
        <Popover
          id={id}
          open={openPopover}
          anchorEl={anchorEl}
          onClose={() => setAnchorEl(null)}
          anchorOrigin={{
            vertical: "bottom",
            horizontal: "left",
          }}
          transformOrigin={{
            vertical: "top",
            horizontal: "left",
          }}
        >
          <div style={{ padding: "10px" }}>
            <DateRangePicker
              ranges={[dateRange || {
                startDate: new Date(),
                endDate: new Date(),
                key: "selection",
            }]}
                onChange={(ranges) => setDateRange(ranges.selection)}
                showSelectionPreview={true}
                moveRangeOnFirstSelection={false}
                months={2}
                horizontal={true}
                direction="horizontal"
                minDate={new Date()}
                staticRanges={customStaticRanges}
                inputRanges={customInputRanges}
            />
          </div>
        </Popover>
      </div>
    );
};
  
export default EventSearchBar;
