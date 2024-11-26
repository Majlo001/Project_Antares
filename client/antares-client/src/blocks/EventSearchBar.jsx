import React, { useState, useEffect, useRef } from "react";
import { Box, Autocomplete, Button, TextField, FormControl, Popover, List, ListItem, ListItemText } from "@mui/material";
import { DateRangePicker } from "react-date-range";
import { format, startOfDay, endOfDay } from 'date-fns';
import 'react-date-range/dist/styles.css';
import 'react-date-range/dist/theme/default.css';
import './../styles/react-date-range-custom.css';
import { request } from "../helpers/axios_helper";
import { Link } from "react-router-dom"
  
const EventSearchBar = () => {
    const [selectedCategory, setSelectedCategory] = useState("");
    const [selectedLocation, setSelectedLocation] = useState("");
    const [dateRange, setDateRange] = useState({
        startDate: new Date(),
        endDate: new Date(),
        key: "selection",
    });

    const [anchorEl, setAnchorEl] = useState(null);
    const [locations, setLocations] = useState([]);

    const [searchText, setSearchText] = useState("");
    const [categories, setCategories] = useState([]);
    const [filteredEvents, setFilteredEvents] = useState([]);
    const [openPopover2, setOpenPopover2] = useState(false);
    const textFieldRef = useRef(null);

    useEffect(() => {
        fetchCategories();
        fetchLocations();
    }, []);

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
  
    const handleDateChange = (ranges) => {
      setDateRange(ranges.selection);
    };

    const handleSearch = () => {
        const formattedStartDate = format(startOfDay(dateRange.startDate), "yyyy-MM-dd'T'HH:mm:ss");
        const formattedEndDate = format(endOfDay(dateRange.endDate), "yyyy-MM-dd'T'HH:mm:ss");

        console.log("Search:", formattedStartDate, formattedEndDate);


        request("GET", `/api/events?category=${selectedCategory}&cityName=${selectedLocation}&dateStart=${formattedStartDate}&dateEnd=${formattedEndDate}&searchText=${searchText}&page=1&size=10`)
            .then((response) => {
                console.log("Pobrano eventy:", response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania eventów:", error);
            });

            
        // const searchParams = new URLSearchParams({
        //     category: selectedCategory,
        //     cityName: selectedLocation,
        //     dateStart: formattedStartDate,
        //     dateEnd: formattedEndDate,
        //     searchText: searchText
        // });

        // window.location.href = `/events?${searchParams.toString()}`;
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
            value={`${format(dateRange.startDate, "MM/dd/yyyy")} - ${format(dateRange.endDate, "MM/dd/yyyy")}`}
            onClick={(event) => setAnchorEl(event.currentTarget)}
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
              ranges={[dateRange]}
              onChange={handleDateChange}
              showSelectionPreview={true}
              moveRangeOnFirstSelection={false}
              months={2}
              horizontal={true}
              minDate={new Date()}
            />
          </div>
        </Popover>
      </div>
    );
  };
  
  export default EventSearchBar;
