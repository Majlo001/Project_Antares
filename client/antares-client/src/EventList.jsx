import React, { useState, useEffect } from 'react';
import { request } from './helpers/axios_helper';
import { useNavigate } from 'react-router-dom';
import { Container, Typography, Paper, Button, Box, Grid } from '@mui/material';
import { serverBaseUrl } from './helpers/settings';
import { formatDate, formatTime } from './helpers/time_format_helper';

import no_image from './images/no_image.png';

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

    const getMainImage = (mainImage) => {
        if (mainImage) {
            return serverBaseUrl + mainImage;
        }
        return no_image;
    }

    useEffect(() => {
        fetchEvents();
    }, []);

    const handlePageChange = (newPage) => {
        fetchEvents(newPage);
    };

    return (
        <Container maxWidth="md" sx={{ mt: 4 }}>
          <Typography variant="h4" component="h2" gutterBottom>
            Lista dostępnych wydarzeń
          </Typography>
    
          <Grid container spacing={2} rowSpacing={6} columnSpacing={2} marginBottom={6}>
            {events.map((event, index) => (
                <Grid item xs={12} sm={6} md={3} key={index}>
                    <Paper 
                        elevation={3} 
                        sx={{ p: 2,
                            height: '100%',
                            cursor: 'pointer',
                            transition: 'transform 0.2s, box-shadow 0.2s',
                            '&:hover': {
                                transform: 'scale(1.02)',
                                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.2)',
                            }, 
                        }}
                        onClick={() => navigate(`/events/${event.id}`)}
                    >
                        <Box
                            component="img"
                            src={getMainImage(event.mainImage)}
                            alt={event.name}
                            sx={{
                                width: '100%',
                                height: 240,
                                objectFit: 'cover',
                                mb: 2,
                                borderRadius: 1,
                            }}
                        />
                        <Typography variant="h5" component="h3">
                            {event.name}
                        </Typography>
                        <Typography variant="body1" color="text.secondary">
                            {event.shortDescription}
                        </Typography>
                        {event.eventDateStart && (
                            <Typography variant="body2" color="text.secondary">
                                {formatDate(event.eventDateStart) + " " + formatTime(event.eventDateStart)}
                            </Typography>
                        )}
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
