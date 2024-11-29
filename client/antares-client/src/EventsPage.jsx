import React, { useEffect, useState } from "react";
import { Container, Grid, CircularProgress } from "@mui/material";
import { useSearchParams, useNavigate } from "react-router-dom";
import { request } from "./helpers/axios_helper";

import EventSearchBar from "./blocks/EventSearchBar";
import EventPreviewItem from "./blocks/EventPreviewItem";

const EventsPage = () => {
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useSearchParams();
    const [events, setEvents] = useState([]);

    const [page, setPage] = useState(1);
    const [size, setSize] = useState(10);
    const [loading, setLoading] = useState(true);

    const fetchEvents = (filters) => {
        setLoading(true);
        filters = filters || {};

        filters.page = page;
        filters.size = size;
        console.log("Filters:", filters);

        request("GET", `/api/events`, null, filters)
            .then((response) => {
                console.log("Fetched events:", response.data.events);
                setEvents(response.data.events || []);
            })
            .catch((error) => {
                console.error("Error fetching events:", error);
            })
            .finally(() => {
                setLoading(false);
            });
    };

    useEffect(() => {
        setLoading(true);
        const filters = Object.fromEntries(searchParams.entries());
        fetchEvents(filters);
    // }, [searchParams]);
    }, []);

    return (
        <Container>
            <Container maxWidth="xl" sx={{ mt: 4 }}>
                <EventSearchBar searchParams={searchParams} />
                {loading ? (
                    <Grid container justifyContent="center" alignItems="center" sx={{ mt: 4 }}>
                        <CircularProgress />
                    </Grid>
                ) : (
                    <Grid container spacing={2} p={4}>
                        {events.length > 0 ? (
                            events.map((event) => (
                                <Grid item xs={12} key={event.id}>
                                    <EventPreviewItem event={event} />
                                </Grid>
                            ))
                        ) : (
                            <p>No events found</p>
                        )}
                    </Grid>
                )}
            </Container>
        </Container>
    );
};

export default EventsPage;

