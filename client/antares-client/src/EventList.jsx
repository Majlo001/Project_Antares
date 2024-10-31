import React, { useState, useEffect } from 'react';
import { request } from './helpers/axios_helper';
import { Container, Typography, Paper, Button, Box, Grid } from '@mui/material';

const EventList = () => {
    const [events, setEvents] = useState([]);
    const [pageNo, setPageNo] = useState(1);
    const [pageSize] = useState(8);
    const [totalPages, setTotalPages] = useState(1);

    // Funkcja do pobierania eventów z serwera
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

    // Pobieranie eventów przy pierwszym renderze
    useEffect(() => {
        fetchEvents();
    }, []);

    // Obsługa zmiany strony
    const handlePageChange = (newPage) => {
        fetchEvents(newPage);
    };

    return (
        <Container maxWidth="md" sx={{ mt: 4 }}>
          <Typography variant="h4" component="h2" gutterBottom>
            Lista dostępnych wydarzeń
          </Typography>
    
          <Grid container spacing={2} rowSpacing={3} columnSpacing={2}>
            {events.map((event, index) => (
                <Grid item xs={12} sm={6} md={3} key={index}>
                    <Paper elevation={3} sx={{ p: 2, height: '100%' }}>
                    <Typography variant="h5" component="h3">
                        {event.name}
                    </Typography>
                    <Typography variant="body1" color="text.secondary">
                        {event.description}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        Date: {new Date(event.dateStart).toLocaleString()}
                    </Typography>
                    </Paper>
                </Grid>
            ))}
        </Grid>
    
          <Box display="flex" justifyContent="space-between" mt={3}>
            {pageNo > 1 && (
              <Button variant="contained" onClick={() => handlePageChange(pageNo - 1)}>
                Poprzednia strona
              </Button>
            )}
            {pageNo < totalPages && (
              <Button variant="contained" onClick={() => handlePageChange(pageNo + 1)}>
                Następna strona
              </Button>
            )}
          </Box>
        </Container>
      );
};

export default EventList;
