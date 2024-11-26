import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Typography, Paper, Button, Box, Grid } from '@mui/material';
import { request } from './helpers/axios_helper';
import EventSearchBar from './blocks/EventSearchBar';
import EventSlider from './blocks/EventSlider';

const EventList = () => {
    const navigate = useNavigate();

    const [events, setEvents] = useState([]);
    const [pageNo, setPageNo] = useState(1);
    const [pageSize] = useState(8);
    const [totalPages, setTotalPages] = useState(1);

    const fetchEvents = (page = 1) => {
        request("GET", `/api/events?page=${page}&size=${pageSize}`)
            .then((response) => {
                console.log("Pobrano eventy:", response.data.events);
                setEvents(response.data.events);
                setPageNo(response.data.pageNo + 1);
                setTotalPages(response.data.totalPages);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania eventów:", error);
            });
    };

    useEffect(() => {
        fetchEvents();
    }, []);

    return (
        <Container maxWidth="xl" sx={{ mt: 4 }}>
            <EventSearchBar />
            <Typography variant="h4" component="h2" gutterBottom>
                Current Events
            </Typography>
    
            <EventSlider
                events={events}
            />
        </Container>
      );
};

export default EventList;
