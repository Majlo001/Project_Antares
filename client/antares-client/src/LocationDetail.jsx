import React, { useEffect, useState } from 'react';
import { Avatar, Grid, Typography, Container, Box } from '@mui/material';
import { request } from "./helpers/axios_helper";
import { useParams } from 'react-router-dom';
import { serverBaseUrl } from './helpers/settings';
import { Button, ButtonGroup } from '@mui/material';
import { Instagram, Facebook, Language } from '@mui/icons-material';

import EventPreviewItem from "./blocks/EventPreviewItem";

const LocationDetail = () => {
    const { locationId } = useParams();
    const [location, setLocation] = useState([]);

    useEffect(() => {
        fetchLocations();
    }, []);

    const fetchLocations = () => {
        request("GET", "/api/locations", null, { locationId: locationId, maxEvents: 10 })
            .then((response) => {
                console.log("Pobrano lokację:", response.data);
                setLocation(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania lokacji:", error);
            });
    }

    return (
        <Container maxWidth="xl" sx={{ mt: 4 }}>
            <Grid container spacing={6}>
                <Grid item xs={4}>
                    <img
                        alt="Location Image"
                        src={serverBaseUrl + location.mainImage}
                        style={{
                            width: '100%',
                            height: 'auto',
                            marginTop: 16,
                            marginBottom: 16
                        }}
                    />
                    <ButtonGroup
                        variant="outlined" 
                        aria-label="outlined button group"
                        sx={{ display: 'flex', justifyContent: 'center' }}
                    >
                        <Button
                            href={location.googleMapsLink}
                            target="_blank"
                            rel="noopener noreferrer"
                        >
                            Show on map
                        </Button>
                        {location.websiteUrl && (
                            <Button
                                href={location.websiteUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                <Language />
                            </Button>
                        )}
                    </ButtonGroup>
                </Grid>
                <Grid item xs={8}>
                    <Typography variant="h4" mb={2}>{location.name}</Typography>
                    <Typography variant="body1" mb={2}>{location.cityName} | {location.address}</Typography>
                    <Typography variant="body1">{location.description}</Typography>

                    <Box sx={{ mt: 4 }}>
                        <iframe
                            width="100%"
                            height="400"
                            frameBorder="0"
                            style={{ border: 0 }}
                            src={`https://www.google.com/maps?q=${encodeURIComponent(location.name + ', ' + location.cityName)}&output=embed`}
                            allowFullScreen
                        ></iframe>
                    </Box>
                </Grid>
            </Grid>
            <Box sx={{ mt: 4, pl: 4 }}>
                <Typography variant="h4">Active events:</Typography>
            </Box>
            <Grid container spacing={2} p={4}>
                {location.events && location.events.map((event) => (
                    <Grid item xs={12} key={event.id}>
                        <EventPreviewItem event={event} />
                    </Grid>
                ))}
            </Grid>

        </Container>
    );
};

export default LocationDetail;