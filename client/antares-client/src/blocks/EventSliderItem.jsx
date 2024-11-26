import React from 'react';
import { Grid, Paper, Box, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { serverBaseUrl } from '../helpers/settings';
import { formatDate, formatTime } from '../helpers/time_format_helper';


import no_image from '../images/no_image.png';

const EventSliderItem = ({ event, index }) => {
    const navigate = useNavigate();

    const getMainImage = (mainImage) => {
        if (mainImage) {
            return serverBaseUrl + mainImage;
        }
        return no_image;
    }

    return (
        <Grid item xs={12} sm={6} md={4} lg={3}>
            <Box 
                elevation={3}
                sx={{
                    margin: 1,
                    p: 1,
                    height: '100%',
                    cursor: 'pointer',
                    transition: 'transform 0.2s, box-shadow 0.2s',
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'space-between',
                    '&:hover': {
                        transform: 'scale(1.02)',
                    }, 
                }}
                onClick={() => navigate(`/events/${event.id}`)}
            >
                <Box>
                    <Box
                        component="img"
                        src={getMainImage(event.mainImage)}
                        alt={event.name}
                        sx={{
                            width: '100%',
                            height: '280px',
                            objectFit: 'cover',
                            mb: 2,
                            borderRadius: 1,
                        }}
                    />
                    <Typography variant="h6" component="h3">
                        {event.name}
                    </Typography>
                    {/* <Typography variant="body2" color="text.secondary" >
                        {event.shortDescription}
                    </Typography> */}
                    <Typography variant="body2" color="text.secondary" marginTop={1} marginBottom={1} >
                        {event.location.city} | {event.location.name}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        {formatDate(event.eventDateStart) + " " + formatTime(event.eventDateStart)}
                    </Typography>
                </Box>
            </Box>
        </Grid>
    );
};

export default EventSliderItem;