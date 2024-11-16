import React, { useEffect, useState } from 'react';
import EventIcon  from '@mui/icons-material/Event';
import PlaceIcon from '@mui/icons-material/Place';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import EventOwnerProfileBlock from "./blocks/EventOwnerProfileBlock";

import { useParams, Link } from 'react-router-dom';
import { Box, Typography, Paper, Button } from '@mui/material';
import { request } from './helpers/axios_helper';
import { formatDate, formatTime } from './helpers/time_format_helper';

const EventDetail = () => {
    const { eventId } = useParams();
    const [event, setEvent] = useState(null);

    useEffect(() => {
        request("GET", `/api/events/event/${eventId}`).then((response) => {
            setEvent(response.data);    
            console.log("Pobrano szczegóły wydarzenia:", response.data);
        }).catch((error) => {
            console.error("Błąd przy pobieraniu szczegółów wydarzenia:", error);
        });
    }, [eventId]);

    if (!event) {
        return <Typography variant="h6">Ładowanie szczegółów wydarzenia...</Typography>;
    }

    // {formatTime(event.eventDateStart)}
    return (
        <Box sx={{ mx: 'auto', mt: 4, p: 2, width: '100%' }}>
            <Typography variant="h2" component="h1">
                {event.name}
            </Typography>
            <Typography
                variant="body1"
                color="text.secondary"
                sx={{ mt: 2 }}
                dangerouslySetInnerHTML={{ __html: event.description }}
            />

            <EventOwnerProfileBlock
                imageUrl={event.eventSeries.eventOwner.image}
                name={event.eventSeries.eventOwner.name}
                id={event.eventSeries.eventOwner.id} />

            <Box sx={{ mt: 2 }}>
                {event.eventDateStart && (
                    <>
                        <Typography variant="body2" color="text.secondary" sx={{ display: 'flex', alignItems: 'center' }}>
                            <EventIcon  sx={{ color: 'black' }} />
                            <span style={{ marginLeft: '8px' }}>{formatDate(event.eventDateStart)}</span>
                        </Typography>
                        <Typography variant="body2" color="text.secondary" sx={{ display: 'flex', alignItems: 'center', mt: 1 }}>
                            <AccessTimeIcon sx={{ color: 'black' }} />
                            <span style={{ marginLeft: '8px' }}>{formatTime(event.eventDateStart)}</span>
                        </Typography>
                    </>
                )}
                <Typography variant="body2" color="text.secondary" sx={{ display: 'flex', alignItems: 'center', mt: 1 }}>
                    <PlaceIcon sx={{ color: 'black' }} />
                    <span style={{ marginLeft: '8px' }}>{event.location.city}, {event.location.name}</span>
                </Typography>
            </Box>

            <Box sx={{ mt: 4 }}>
                <Button
                    variant="contained"
                    color="primary"
                    component={Link}
                    to={`/seat_chart/${eventId}`}
                >
                    Check seat availability
                </Button>
            </Box>
        </Box>
    );
};

export default EventDetail;