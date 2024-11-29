import React from "react";
import { Grid, Typography, Card, CardMedia, CardContent, Box, Button } from "@mui/material";
import { format } from "date-fns";
import { formatDateTime } from "../helpers/time_format_helper";
import { serverBaseUrl } from "../helpers/settings";
import { Link, useNavigate } from "react-router-dom";
import EventSeatRoundedIcon from '@mui/icons-material/EventSeatRounded';

const EventPreviewItem = ({ event }) => {
    const navigate = useNavigate();


    const handleCardClick = () => {
        navigate(`/events/${event.id}`);
    };

    return (
        <Card
            onClick={handleCardClick}
            style={{
                display: "flex",
                marginBottom: "20px",
                alignItems: "center",
                // backgroundColor: "#f4f4f4",
                transition: "box-shadow 0.3s ease-in-out",
                cursor: "pointer"
            }}
            sx={{
                '&:hover': {
                    boxShadow: 6,
                }
            }}
        >
            <Grid container spacing={6} alignItems="center">
                <Grid item xs={3}>
                    <CardMedia
                        component="img"
                        alt={event.name}
                        image={serverBaseUrl + event.mainImage}
                        style={{
                            width: "auto",
                            height: "240px",
                            objectFit: "cover",
                            margin: "0 auto",
                            padding: 4
                        }}
                    />
                </Grid>
            
                <Grid item xs={9}>
                    <CardContent style={{ paddingLeft: "20px" }}>
                        <Box
                            sx={{
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'center',
                                flexWrap: 'nowrap',
                                gap: 2,
                            }}
                        >
                            <Typography
                                variant="h5"
                                component="div"
                                sx={{
                                    fontWeight: 'bold',
                                    flex: '1 1 auto',
                                    minWidth: '0',
                                    overflow: 'hidden',
                                    textOverflow: 'ellipsis',
                                }}
                            >
                                {event.name}
                            </Typography>

                            <Box
                                sx={{
                                    flexShrink: 0,
                                }}
                            >
                                <Button
                                    variant="outlined"
                                    color="primary"
                                    component={Link}
                                    to={`/seat_chart/${event.id}`}
                                    sx={{ color: 'text.primary', borderColor: 'primary.main' }}
                                    startIcon={<EventSeatRoundedIcon />}
                                    onClick={(e) => e.stopPropagation()}
                                >
                                    Check seat availability
                                </Button>
                            </Box>
                        </Box>

                        <Typography variant="body2" color="text.secondary" style={{ marginTop: "8px" }}>
                            {event.shortDescription}
                        </Typography>

                        <Typography variant="body2" color="text.secondary" style={{ marginTop: "8px" }}>
                            {formatDateTime(event.eventDateStart)}
                        </Typography>

                        <Typography variant="body2" color="text.secondary" style={{ marginTop: "8px" }}>
                            {event.location.city} | {event.location.name}
                        </Typography>

                        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mt: 1, mb: 1 }}>
                            <Button
                                variant="outlined"
                                color="primary"
                                sx={{ textTransform: 'none' }}
                            >
                                {event.eventSeries.category.eventCategoryName}
                            </Button>
                        </Box>
                    </CardContent>
                </Grid>
            </Grid>
        </Card>
    );
};

export default EventPreviewItem;
