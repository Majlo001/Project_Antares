import React, { useEffect, useState } from 'react';
import { Avatar, Grid, Typography, Container, Box } from '@mui/material';
import { request } from "./helpers/axios_helper";
import { useParams } from 'react-router-dom';
import { serverBaseUrl } from './helpers/settings';
import { Button, ButtonGroup } from '@mui/material';
import { Instagram, Facebook, Language } from '@mui/icons-material';

import spotify_icon from './icons/icons8-spotify.svg';
import EventPreviewItem from "./blocks/EventPreviewItem";


const ArtistDetail = () => {
    const { artistId } = useParams();
    const [artist, setArtist] = useState([]);

    useEffect(() => {
        fetchArtists();
    }, []);

    const fetchArtists = () => {
        request("GET", "/api/artists", null, { artistId: artistId, maxEvents: 10 })
            .then((response) => {
                console.log("Pobrano artystów:", response.data);
                setArtist(response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania artystów:", error);
            });
    }

    return (
        <Container maxWidth="xl" sx={{ mt: 4 }}>
            <Grid container spacing={6} alignItems="center">
                <Grid item xs={3}>
                    <Avatar
                        alt="Artist Image"
                        src={serverBaseUrl + artist.mainImage}
                        sx={{
                            width: '100%',
                            height: 'auto',
                            marginBottom: 2
                        }}
                    />
                    <ButtonGroup
                        variant="outlined" 
                        aria-label="outlined button group"
                        sx={{ display: 'flex', justifyContent: 'center' }}
                    >
                        {artist.websiteUrl && (
                            <Button
                                href={artist.websiteUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                <Language />
                            </Button>
                        )}
                        {artist.instagramUrl && (
                            <Button
                                href={artist.instagramUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                <Instagram />
                            </Button>
                        )}
                        {artist.facebookUrl && (
                            <Button
                                href={artist.facebookUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                <Facebook />
                            </Button>
                        )}
                        {artist.spotifyUrl && (
                            <Button
                                href={artist.spotifyUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                <img src={spotify_icon} alt="Spotify" style={{ width: 24, height: 24 }} />
                            </Button>
                        )}
                    </ButtonGroup>
                </Grid>
                <Grid item xs={9}>
                    <Typography variant="h2" mb={2}>{artist.name}</Typography>
                    <Typography variant="body1">{artist.description}</Typography>
                </Grid>
            </Grid>
            <Box sx={{ mt: 4, pl: 4 }}>
                <Typography variant="h4">Active events:</Typography>
            </Box>
            <Grid container spacing={2} p={4}>
                {artist.events && artist.events.map((event) => (
                    <Grid item xs={12} key={event.id}>
                        <EventPreviewItem event={event} />
                    </Grid>
                ))}
            </Grid>

        </Container>
    );
};

export default ArtistDetail;