import React, { useEffect, useState } from "react";
import {
    Box,
    Typography,
    Grid,
    Card,
    CardHeader,
    CardContent,
    CardActions,
    Button,
    Divider,
    Chip,
    CircularProgress,
} from "@mui/material";
import {
    Event as EventIcon,
    Chair as ChairIcon,
    Download as DownloadIcon,
    ConfirmationNumber as ConfirmationNumberIcon,
    Place as PlaceIcon,
    EventSeat as EventSeatIcon,
    Launch as LaunchIcon
} from "@mui/icons-material";
import { request } from "./helpers/axios_helper";
import { getDateFromArray, formatDateTime } from "./helpers/time_format_helper";

const UserTickets = () => {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        request("GET", `/api/tickets/all`)
            .then((response) => {
                setTickets(response.data);
                console.log("Tickets fetched:", response.data);
            })
            .catch((error) => {
                console.error("Error fetching tickets:", error);
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    const activeTickets = tickets.filter((ticket) => getDateFromArray(ticket.eventDate) >= new Date());
    const expiredTickets = tickets.filter((ticket) => getDateFromArray(ticket.eventDate) < new Date());

    const openTicketInNewTab = (ticketPdfLink) => {
        window.open(ticketPdfLink, "_blank");
    };

    const downloadTicket = async (ticketPdfLink) => {
        try {
            const response = await fetch(ticketPdfLink);
            if (!response.ok) {
                throw new Error("Błąd pobierania pliku");
            }
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
    
            const link = document.createElement("a");
            link.href = url;
            const fileName = ticketPdfLink.split('/').pop();
            link.download = fileName;
            document.body.appendChild(link);
            link.click();
    
            link.remove();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Błąd podczas pobierania pliku:", error);
        }
    }

  return (
    <Grid sx={{ padding: 3, width: "100%" }}>
        <Typography variant="h4" gutterBottom>Your tickets:</Typography>
        {loading ? (
            <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: "50vh" }}>
                <CircularProgress />
            </Box>
        ) : (
            <Grid container spacing={3}>
                
                {/* Aktywne bilety */}
                <Grid item xs={12}>
                    <Typography variant="h5" gutterBottom>
                        Active tickets
                    </Typography>
                    <Divider sx={{ mb: 2 }} />
                    <Grid container spacing={3}>
                    {activeTickets.length > 0 ? (
                        activeTickets.map((ticket) => (

                        <Grid item sm={12} md={6} lg={4} key={ticket.ticketId}>
                            <Card
                                sx={{
                                    border: "1px solid #81c784",
                                    position: "relative",
                                }}
                            >
                                <Box
                                    sx={{
                                        position: "absolute",
                                        top: 0,
                                        right: 0,
                                        backgroundColor: ticket.isValidated ? "#e57373" : "#81c784",
                                        color: "#fff",
                                        padding: "4px 8px",
                                        borderRadius: "0 0 0 8px",
                                        display: "flex",
                                        alignItems: "center",
                                    }}
                                >
                                    {ticket.isValidated ? (
                                        <>
                                            <ConfirmationNumberIcon sx={{ mr: 1 }} />
                                            <Typography variant="body2">Used</Typography>
                                        </>
                                    ) : (
                                        <>
                                            <ChairIcon sx={{ mr: 1 }} />
                                            <Typography variant="body2">Not Used</Typography>
                                        </>
                                    )}
                                </Box>
                                <Box p={2}>
                                    <a 
                                        href={"/events/" + ticket.eventId}
                                        target="_blank" 
                                        rel="noopener noreferrer" 
                                        style={{ textDecoration: "none", color: "inherit" }}
                                    >
                                        <Typography
                                            variant="h6"
                                            color="text.primary"
                                            p={2}
                                            sx = {{ pb: 0 }}
                                        >
                                            <strong>{ticket.eventName}</strong>
                                        </Typography>
                                    </a>
                                    <CardHeader
                                        title={formatDateTime(ticket.eventDate)}
                                        avatar={<EventIcon color="primary" />}
                                        sx = {{ pb: 0 }}
                                    />
                                    <a 
                                        href={"/locations/" + ticket.eventLocationId}
                                        target="_blank" 
                                        rel="noopener noreferrer" 
                                        style={{ textDecoration: "none", color: "inherit" }}
                                    >
                                        <CardHeader
                                            title={ticket.eventLocation}
                                            avatar={<PlaceIcon color="primary" />}
                                            sx={{ pb: 0 }}
                                        />
                                    </a>
                                    <CardContent>
                                        <Grid container spacing={2} alignItems="start">
                                            <Grid item>
                                                <EventSeatIcon color="primary"/>
                                            </Grid>

                                            {/* Kolumna z treścią */}
                                            <Grid item xs>
                                                <Grid container spacing={1}>
                                                    <Grid item xs={12}>
                                                        <Typography variant="body2">
                                                            Sector: <strong>{ticket.sectorName}</strong>
                                                        </Typography>
                                                    </Grid>
                                                    <Grid item xs={12}>
                                                        <Typography variant="body2">
                                                            Row: <strong>{ticket.rowNumber}</strong>
                                                        </Typography>
                                                    </Grid>
                                                    <Grid item xs={12}>
                                                        <Typography variant="body2">
                                                            seat: <strong>{ticket.seatNumber}</strong>
                                                        </Typography>
                                                    </Grid>
                                                </Grid>

                                                <Divider sx={{ my: 1 }} />

                                                <Grid container spacing={1}>
                                                    <Grid item xs={12}>
                                                        <Typography variant="body2">
                                                            Price: <strong>{ticket.ticketPrice.toFixed(2)} PLN</strong>
                                                        </Typography>
                                                    </Grid>
                                                    <Grid item xs={12}>
                                                        <Typography variant="body2">
                                                            Ticket type: <strong>{ticket.ticketTypeName}</strong>
                                                        </Typography>
                                                    </Grid>
                                                    <Grid item xs={12}>
                                                        <Typography variant="body2">
                                                            Ticket number: <strong>{ticket.ticketId}</strong>
                                                        </Typography>
                                                    </Grid>
                                                </Grid>
                                            </Grid>
                                        </Grid>
                                    </CardContent>

                                    <Divider sx={{ my: 1 }} />

                                    <CardActions>
                                        <Box sx={{ display: "flex", justifyContent: "flex-end", width: "100%" }}>
                                            <Button
                                                color="gray"
                                                onClick={() => openTicketInNewTab(ticket.ticketPdfLink)}
                                                target="_blank"
                                                startIcon={<LaunchIcon />}
                                                p={0}
                                                sx={{
                                                    minWidth: 0,
                                                    '& .MuiButton-startIcon': {
                                                        margin: 1,
                                                    },
                                                    '&:hover': {
                                                        color: 'primary.main'
                                                    },
                                                }}
                                            />

                                            <Button
                                                color="gray"
                                                onClick={() => downloadTicket(ticket.ticketPdfLink)}
                                                target="_blank"
                                                download
                                                startIcon={<DownloadIcon />}
                                                p={0}
                                                sx={{
                                                    minWidth: 0,
                                                    '& .MuiButton-startIcon': {
                                                        margin: 1,
                                                    },
                                                    '&:hover': {
                                                        color: 'primary.main'
                                                    },
                                                }}
                                            />
                                        </Box>
                                    </CardActions>
                                </Box>
                            </Card>
                        </Grid>
                        ))
                    ) : (
                        <Typography color="text.secondary" p={3}>No active tickets.</Typography>
                    )}
                    </Grid>
                </Grid>

                {/* Wygasłe bilety */}
                <Grid item xs={12}>
                    <Typography variant="h5" gutterBottom>
                    Expired tickets
                    </Typography>
                    <Divider sx={{ mb: 2 }} />
                    <Grid container spacing={3}>
                    {expiredTickets.length > 0 ? (
                        expiredTickets.map((ticket) => (
                            <Grid item sm={12} md={6} lg={4} key={ticket.ticketId}>
                            </Grid>
                        ))
                    ) : (
                        <Typography color="text.secondary" p={3}>No expired tickets.</Typography>
                    )}
                    </Grid>
                </Grid>
            </Grid>
        )}
        </Grid>

  );
};

export default UserTickets;
