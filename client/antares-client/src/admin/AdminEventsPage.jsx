import React, { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import {
    Container,
    Button,
    TextField,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    IconButton,
    Typography,
    Paper,
    Box,
    MenuItem,
    Select,
    FormControl,
    Autocomplete,
    Grid,

} from "@mui/material";

import CheckCircleRoundedIcon from "@mui/icons-material/CheckCircleRounded";
import UnpublishedRoundedIcon from "@mui/icons-material/UnpublishedRounded";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import LaunchIcon from "@mui/icons-material/Launch";
import AddIcon from "@mui/icons-material/Add";

import { format, startOfDay, endOfDay } from 'date-fns';
import { DateRangePicker } from "react-date-range";
import "react-date-range/dist/styles.css"; // Główne style
import "react-date-range/dist/theme/default.css";

import { request } from "../helpers/axios_helper";
import { formatDateTime, formatDateToLocal } from "../helpers/time_format_helper";

const AdminEventsPage = () => {
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useSearchParams();

    const [locations, setLocations] = useState([]);
    const [categories, setCategories] = useState([]);
    const [events, setEvents] = useState([]);
    const [statuses, setStatuses] = useState([]);
    const [publicTypes, setPublicTypes] = useState(["Public", "Private"]);

    const [filters, setFilters] = useState({
        searchText: searchParams.get("searchText") || "",
        cityName: searchParams.get("cityName") || "",
        dateStart: searchParams.get("dateStart") || "",
        dateEnd: searchParams.get("dateEnd") || "",
        category: searchParams.get("category") || "",
        status: searchParams.get("status") || "",
        type: searchParams.get("type") || "",
    });

    useEffect(() => {
        console.log("Filters:", filters);
    }, [filters]);

    useEffect(() => {
        console.log("Events:", events);
    }, [events]);

    const fetchEvents = () => {
        const params = Object.fromEntries(searchParams.entries());
        if (params.dateStart) {
            const startDate = new Date(params.dateStart);
            params.dateStart = format(startOfDay(startDate), "yyyy-MM-dd'T'HH:mm:ss");
        }
    
        if (params.dateEnd) {
            const endDate = new Date(params.dateEnd);
            params.dateEnd = format(endOfDay(endDate), "yyyy-MM-dd'T'HH:mm:ss");
        }

        if (params.type) {
            params.isPublic = params.type === "Public";
            delete params.type;
        }



        console.log("Params:", params);
        console.log(`/api/events/owner_preview?${new URLSearchParams(params)}`)
        request("GET", `/api/events/owner_preview?${new URLSearchParams(params)}`)
            .then((response) => {
                console.log("Event created:", response.data);
                setEvents(response.data.events || []);
            })
            .catch(error => {
                console.error("Error fetching events:", error);
            });
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

    const fetchStatuses = () => {
        request("GET", "/api/dicts/event_statuses")
            .then((response) => {
                setStatuses(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania statusów:", error);
            });
    }

    useEffect(() => {
        fetchLocations();
        fetchCategories();
        fetchStatuses();
        fetchEvents();
    }, []);

    const handleDateRangeChange = ({ selection }) => {
        const dateStart = format(selection.startDate, "yyyy-MM-dd");
        const dateEnd = format(selection.endDate, "yyyy-MM-dd");
    
        setFilters((prev) => ({
            ...prev,
            dateStart,
            dateEnd,
        }));

        if (dateStart && dateEnd) {
            const params = new URLSearchParams(searchParams);
            params.set("dateStart", dateStart);
            params.set("dateEnd", dateEnd);
            setSearchParams(params);
        }
    };
    
    const handleFilterChange = (field, value) => {
        const newFilters = { ...filters, [field]: value };
    
        setFilters(newFilters);
    
        const updatedParams = {};
        for (const key in newFilters) {
            if (newFilters[key]) {
                updatedParams[key] = newFilters[key];
            }
        }
        setSearchParams(updatedParams);
    };

    useEffect(() => {
        fetchEvents();
    }, [searchParams]);

//   const handleDelete = (id) => {
//     console.log("Deleting event with ID:", id);
//   };

    const handleLaunch = (id) => {
        window.open(`/events/${id}`, '_blank');
    };

    const handleEdit = (id) => {
        navigate(`/admin/form/event/${id}`);
    };

  return (
    <Container sx={{ maxWidth: '1400px', width: '100%', mx: 'auto', mt: 4, mb:4, p: 4 }}>
      <Typography variant="h4" gutterBottom>
        Event Management
      </Typography>
      {/* Add Event Button */}
      <Button
        variant="contained"
        color="primary"
        startIcon={<AddIcon />}
        style={{ marginBottom: "16px" }}
        onClick={() => console.log("Navigate to create event page")}
      >
        Add New Event
      </Button>

      <Grid container spacing={2} mb={4}>
        {/* Row 1 */}
        <Grid item xs={12} sm={4}>
          <TextField
            fullWidth
            label="Event name"
            name="Event name"
            variant="outlined"
            size="normal"
            value={filters.name}
            onChange={(e) => handleFilterChange("searchText", e.target.value)}
          />
        </Grid>
        <Grid item xs={12} sm={4}>
          <FormControl fullWidth variant="outlined">
            <Autocomplete
              value={filters.category || ""}
              onChange={(event, value) => handleFilterChange("category", value) }
              options={categories.map((category) => category.name)}
              renderInput={(params) => (
                <TextField {...params} label="Category" variant="outlined" />
              )}
            />
          </FormControl>
        </Grid>
        <Grid item xs={12} sm={4}>
          <FormControl fullWidth variant="outlined">
            <Autocomplete
              value={filters.cityName || ""}
              onChange={(event, value) => handleFilterChange("cityName", value) }
              options={locations.map((location) => location.name)}
              renderInput={(params) => (
                <TextField {...params} label="Location" variant="outlined" />
              )}
            />
          </FormControl>
        </Grid>

        <Grid item xs={12} sm={4}>
            <div style={{ position: "relative" }}>
                <TextField
                    fullWidth
                    label="Date Range"
                    variant="outlined"
                    value={
                        filters.dateStart && filters.dateEnd
                        ? `${new Date(filters.dateStart).toLocaleDateString()} - ${new Date(filters.dateEnd).toLocaleDateString()}`
                        : ""
                    }
                    placeholder="Select Date Range"
                    onClick={() =>
                        setFilters((prev) => ({
                            ...prev,
                            showDatePicker: !prev.showDatePicker,
                        }))
                    }
                    InputProps={{
                        readOnly: true,
                    }}
                />
                {filters.showDatePicker && (
                <div
                    style={{
                        position: "absolute",
                        zIndex: 2,
                        background: "white",
                        borderRadius: "4px",
                        boxShadow: "0px 4px 10px rgba(0,0,0,0.1)",
                        marginTop: "8px",
                    }}
                >
                    <DateRangePicker
                        ranges={[
                            {
                                startDate: filters.dateStart  ? new Date(filters.dateStart) : new Date(),
                                endDate: filters.dateEnd  ? new Date(filters.dateEnd ) : new Date(),
                                key: "selection",
                            },
                        ]}
                        onChange={handleDateRangeChange}
                        months={2}
                        horizontal={true}
                        direction="horizontal"
                    />
                </div>
                )}
            </div>
        </Grid>

        
        
        
        <Grid item xs={12} sm={4}>
          <FormControl fullWidth variant="outlined">
            <Autocomplete
              value={filters.status || ""}
              onChange={(event, value) => handleFilterChange("status", value) }
              options={statuses.map((status) => status.name)}
              renderInput={(params) => (
                <TextField {...params} label="Status" variant="outlined" />
              )}
            />
          </FormControl>
        </Grid>
        <Grid item xs={12} sm={4}>
          <FormControl fullWidth variant="outlined">
            <Autocomplete
              value={filters.type || ""}
              onChange={(event, value) => handleFilterChange("type", value) }
              options={publicTypes.map((type) => type)}
              renderInput={(params) => (
                <TextField {...params} label="Type" variant="outlined" />
              )}
            />
          </FormControl>
        </Grid>
      </Grid>

      {/* Event Table */}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>#</TableCell>
              <TableCell>Name</TableCell>
              <TableCell>Date</TableCell>
              <TableCell>City</TableCell>
              <TableCell>Category</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Public</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {events.map((event, index) => (
              <TableRow key={event.id}>
                <TableCell>{index + 1}</TableCell>
                <TableCell>{event.eventName}</TableCell>
                <TableCell>{formatDateTime(event.eventDateStart)}</TableCell>
                <TableCell>{event.city}</TableCell>
                <TableCell>{event.category}</TableCell>
                <TableCell>{event.status}</TableCell>
                <TableCell>
                    {event.isPublic ? (
                        <CheckCircleRoundedIcon style={{ color: 'green' }} />
                    ) : (
                        <UnpublishedRoundedIcon style={{ color: 'red' }} />
                    )}
                </TableCell>
                <TableCell>
                  <IconButton onClick={() => handleEdit(event.eventId)}>
                    <EditIcon />
                  </IconButton>
                  {/* <IconButton onClick={() => handleDelete(event.id)} color="error">
                    <DeleteIcon />
                  </IconButton> */}
                  <IconButton onClick={() => handleLaunch(event.eventId)}>
                    <LaunchIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
};

export default AdminEventsPage;
