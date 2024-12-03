import React, { useEffect, useState } from 'react';
import { Container,
    Typography,
    Button,
    CircularProgress,
    Box,
    Accordion,
    AccordionSummary,
    AccordionDetails,
    TextField
} from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { request } from '../helpers/axios_helper';

import SeatsChart from '../chartComponents/SeatsChart';
import TicketsChart from '../chartComponents/TicketsChart';


const AdminEventDashboard = () => {
    const { eventId } = useParams();
    const navigate = useNavigate();

    const [loading, setLoading] = useState(true);
    const [eventName, setEventName] = useState('');
    const [isPublic, setIsPublic] = useState(false);
    const [isEventSeatStatusesCreated, setIsEventSeatStatusesCreated] = useState(false);
    const [locationName, setLocationName] = useState('');
    const [locationVariantName, setLocationVariantName] = useState('');
    const [sectors, setSectors] = useState([]);

    const [ticketTypesOptions, setTicketTypesOptions] = useState([]);
    const [canCreateEventSeatStatuses, setCanCreateEventSeatStatuses] = useState(false);

    useEffect(() => {
        setLoading(true);
        
        Promise.all([fetchTicketTypes()])
            .then((ticketTypes) => {
                fetchEventDetails(ticketTypes);
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania danych:", error);
            });
    }, []);

    const updatePrices = () => {
        request("POST", `/api/events/update_ticket_prices/${eventId}`, sectors)
            .then((response) => {
                console.log("Prices updated", response.data);
            })
            .catch((error) => {
                console.error("Błąd podczas aktualizacji cen:", error);
            });

    }
        
    const fetchEventDetails = (ticketTypes) => {
        request("GET", `/api/events/event_dashboard/${eventId}`, null)
            .then((response) => {
                console.log("Event", response.data);
                setEventName(response.data.name);
                setIsPublic(response.data.isPublic);
                setIsEventSeatStatusesCreated(response.data.isEventSeatStatusesCreated);
                setLocationName(response.data.locationName);
                setLocationVariantName(response.data.locationVariantName);
                setSectors(response.data.sectors);


                if (!response.data.isEventSeatStatusesCreated) {
                    const allPricesValid = response.data.sectors.every((sector) =>
                        sector.ticketPrices.some(
                            (ticket) => {
                                if (ticket.ticketTypeName === "Normal" && ticket.price > 0) {
                                    return true;   
                                }
                            }
                        )
                    );
        
                    setCanCreateEventSeatStatuses(allPricesValid);
                }
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania eventu:", error);
            })
            .finally(() => {
                setLoading(false);
            });
    };

    const fetchTicketTypes = () => {
        return request("GET", "/api/dicts/ticket_types")
            .then((response) => {
                setTicketTypesOptions(response.data);
                return response.data;
            })
            .catch((error) => {
                console.error("Błąd podczas pobierania typów biletów:", error);
                throw error;
            });
    };

    const handleEditClick = () => {
        navigate(`/admin/form/event/${eventId}`);
    };

    const handlePriceChange = (sectorId, ticketId, ticketTypeId, ticketTypeName, newPrice) => {
        setSectors((prevSectors) =>
            prevSectors.map((sector) =>
                sector.id === sectorId
                    ? {
                          ...sector,
                          ticketPrices: ticketId
                              ? sector.ticketPrices.map((ticket) =>
                                    ticket.id === ticketId
                                        ? { ...ticket, price: newPrice }
                                        : ticket
                                )
                              : [
                                    ...sector.ticketPrices,
                                    {
                                        id: Date.now(),
                                        ticketTypeId,
                                        ticketTypeName,
                                        price: newPrice,
                                    },
                                ],
                      }
                    : sector
            )
        );
        
        console.log(sectors);
    };

    const handleCreateEventSeatStatusesClick = () => {
        request("POST", `/api/events/generateEventSeatStatusEntities?event_id=${eventId}`)
            .then((response) => {
                console.log("Event seat statuses created", response.data);
                setIsEventSeatStatusesCreated(true);
            })
            .catch((error) => {
                console.error("Błąd podczas tworzenia statusów miejsc:", error);
            })
    }


    

    return (
        <Container sx={{ maxWidth: '1400px', width: '100%', mx: 'auto', mt: 4, mb: 4, p: 4 }}>
            {loading ? (
                <Box display="flex" justifyContent="center" mt={4}>
                    <CircularProgress />
                </Box>
            ) : (
                <>
                    <Typography variant="h4">{eventName}</Typography>
                    <Button variant="contained" color="primary" onClick={handleEditClick} component="button">
                        Edit Event Data
                    </Button>
                    <Box mt={2}>
                        <Typography variant="body1">Location Name: {locationName}</Typography>
                        <Typography variant="body1">Location Variant Name: {locationVariantName}</Typography>
                    </Box>

                    {!isEventSeatStatusesCreated ? (
                        <>
                            {canCreateEventSeatStatuses && (
                                <Typography variant="body1" color='warning'>
                                    You need to set prices (at least for type Normal) for all sectors to create event seat statuses
                                </Typography>
                            )}
                            <Button
                                variant="contained"
                                color="primary"
                                onClick={() => handleCreateEventSeatStatusesClick()}
                                component="button"
                                sx={{ mt: 2 }}
                                disabled={!canCreateEventSeatStatuses}
                            >
                                Create Event Seat Statuses
                            </Button>
                        </>
                    ) : (
                        <>
                            <Typography variant="h4" mt={4} mb={2}>
                                {eventName} statistics
                            </Typography>
                            <Box display="flex" width="100%">
                                <Box width="33%">
                                    <SeatsChart eventId={eventId} />
                                </Box>
                                <Box width="67%">
                                    <TicketsChart eventId={eventId} />
                                </Box>
                            </Box>
                        </>
                    )}
                    

                    <Box mt={2}>
                        <Typography variant="h6">Sectors:</Typography>
                        {sectors.map((sector) => (
                            <Accordion key={sector.id}>
                                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                    <Typography variant="body1">Name: {sector.name}</Typography>
                                </AccordionSummary>
                                <AccordionDetails>
                                    <Typography variant="body1">Standing: {sector.isStanding ? 'Yes' : 'No'}</Typography>
                                    <Typography variant="body1">Ticket Prices:</Typography>
                                    {ticketTypesOptions.map((ticketType) => {
                                        const existingTicket = sector.ticketPrices.find(
                                            (ticket) => ticket.ticketTypeName === ticketType.name
                                        );

                                        return (
                                            <Box key={ticketType.id} display="flex" alignItems="center" ml={2}>
                                                <Typography variant="body1" sx={{ mr: 1 }}>Type: {ticketType.name}</Typography>
                                                <TextField
                                                    type="number"
                                                    value={existingTicket ? existingTicket.price : 0}
                                                    onChange={(e) =>
                                                        handlePriceChange(
                                                            sector.id,
                                                            existingTicket ? existingTicket.id : null,
                                                            ticketType.id,
                                                            ticketType.name,
                                                            e.target.value
                                                        )
                                                    }
                                                    InputProps={{ inputProps: { min: 0 } }}
                                                    variant="outlined"
                                                    size="small"
                                                    sx={{ width: '100px', mr: 1 }}
                                                />
                                                <Typography variant="body1">PLN</Typography>
                                            </Box>
                                        );
                                    })}
                                </AccordionDetails>
                            </Accordion>
                        ))}
                    </Box>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={updatePrices}
                        component="button"
                        sx={{ mt: 2 }}
                    >
                        Save sector prices
                    </Button>
                </>
            )}
        </Container>
    );
};

export default AdminEventDashboard;