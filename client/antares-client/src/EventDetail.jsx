import React, { useEffect, useState } from 'react';
import EventIcon  from '@mui/icons-material/Event';
import PlaceIcon from '@mui/icons-material/Place';
import EventSeatRoundedIcon from '@mui/icons-material/EventSeatRounded';
import AccessTimeIcon from '@mui/icons-material/AccessTime';

import EventOwnerProfileBlock from "./blocks/EventOwnerProfileBlock";
import ArtistCard from './blocks/ArtistCard';
import LocationCard from './blocks/LocationCard';

import { useParams, Link } from 'react-router-dom';
import { Box, Typography, Paper, Button, Grid, Container } from '@mui/material';
import { request } from './helpers/axios_helper';
import { formatDate, formatTime } from './helpers/time_format_helper';
import { useNavigate } from 'react-router-dom';
import { serverBaseUrl } from "./helpers/settings";


const EventDetail = () => {
    const { eventId } = useParams();
    const [event, setEvent] = useState(null);
    
    const navigate = useNavigate();

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
        <Grid container spacing={2} sx={{ mt: 4 }}>
            <Grid item xs={12} md={3}>
                <Box sx={{ mx: 'auto', p: 2 }}>
                    <Box
                        component="img"
                        sx={{
                            width: '100%',
                            height: 'auto',
                            maxHeight: 400,
                            objectFit: 'cover',
                            mb: 2
                        }}
                        alt={event.name}
                        src={serverBaseUrl + event.mainImage}
                    />

                    <Box sx={{ mt: 2 }}>
                        <Button
                            variant="outlined"
                            color="primary"
                            component={Link}
                            to={`/seat_chart/${eventId}`}
                            sx={{ width: '100%', color: 'text.primary', borderColor: 'primary.main' }}
                            startIcon={<EventSeatRoundedIcon />}
                        >
                            Check seat availability
                        </Button>
                    </Box>
                </Box>
            </Grid>
            <Grid item xs={12} md={9}>
                <Container sx={{ padding: 4 }}>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                        <Typography variant="h2" component="h1">
                            {event.name}
                        </Typography>

                        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mt: 1, mb: 1 }}>
                            <Button
                                variant="outlined"
                                color="primary"
                                sx={{ textTransform: 'none' }}
                            >
                                {event.eventSeries.eventCategory.eventCategoryName}
                            </Button>
                        </Box>

                        <Box>
                            {event.eventDateStart && (
                                <>
                                    <Typography variant="body2" color="text.secondary" sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                                        <EventIcon sx={{ color: 'black' }} />
                                        <span style={{ marginLeft: '8px' }}>{formatDate(event.eventDateStart)}</span>
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary" sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                                        <AccessTimeIcon sx={{ color: 'black' }} />
                                        <span style={{ marginLeft: '8px' }}>{formatTime(event.eventDateStart)}</span>
                                    </Typography>
                                </>
                            )}
                            <Typography variant="body2" color="text.secondary" sx={{ display: 'flex', alignItems: 'center' }}>
                                <PlaceIcon sx={{ color: 'black' }} />
                                <span style={{ marginLeft: '8px' }}>
                                    {event.location.city}, {event.location.name}
                                </span>
                            </Typography>
                        </Box>

                        <EventOwnerProfileBlock
                            imageUrl={event.eventSeries.eventOwner.image}
                            name={event.eventSeries.eventOwner.name}
                            id={event.eventSeries.eventOwner.id}
                        />

                        {event.eventSeries.youtubePreviewUrl && (
                            <Box sx={{ mt: 2 }}>
                                <iframe
                                    width="100%"
                                    height="400"
                                    src={`https://www.youtube.com/embed/${event.eventSeries.youtubePreviewUrl.split('v=')[1]}`}
                                    title="YouTube video player"
                                    frameBorder="0"
                                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                    allowFullScreen
                                ></iframe>
                            </Box>
                        )}

                        <Typography
                            variant="body1"
                            color="text.secondary"
                            sx={{ mt: 2 }}
                            dangerouslySetInnerHTML={{ __html: event.description }}
                        />
                    </Box>
                </Container>
            </Grid>
            
            <Grid container spacing={2} p={4}>
                <Grid  item xs={12}>
                    <Typography variant="h4" gutterBottom>
                        Artists
                    </Typography>
                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 4, margin: "32px 0" }}>
                        {event.eventSeries.artists.map((artist) => (
                            <ArtistCard
                                navigate={navigate}
                                key={artist.id}
                                artistId={artist.id}
                                artistName={artist.name}
                                artistImage={artist.mainImage}
                            />
                        ))}
                    </Box>
                </Grid>

                {/* Lokalizacja */}
                <Grid item xs={12}>
                    <Typography variant="h4" gutterBottom>
                        Location
                    </Typography>
                    <LocationCard
                        navigate={navigate}
                        locationId={event.location.id}
                        locationName={event.location.name}
                        locationAddress={event.location.address}
                        locationCity={event.location.city}
                        locationCountry={event.location.country}
                        locationImage={event.location.mainImage}
                        mapLink={event.location.googleMapsLink}
                    />
                </Grid>

                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mt: 1 }}>
                    {event.eventSeries.eventTags.map((tag) => (
                        <Button
                            key={tag.id}
                            variant="outlined"
                            color="primary"
                            sx={{ textTransform: 'none' }}
                        >
                            {tag.name}
                        </Button>
                    ))}
                </Box>
            </Grid>
        </Grid>
    );
};

export default EventDetail;