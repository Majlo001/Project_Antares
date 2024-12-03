import React, { useContext, useEffect, useState } from 'react';
import { Box, Typography, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Select, MenuItem } from '@mui/material';
import { CartContext } from './contexts/CartContext';
import { useNavigate } from 'react-router-dom';
import { request } from "./helpers/axios_helper";
import { formatDateTime } from "./helpers/time_format_helper";

const Cart = () => {
    const { cartData, updateCart } = useContext(CartContext);
    const navigate = useNavigate();

    const [eventName, setEventName] = useState('');
    const [eventLocation, setEventLocation] = useState('');
    const [eventDate, setEventDate] = useState('');
    const [eventShortDescription, setEventShortDescription] = useState('');
    const [sectorsInfo, setSectorsInfo] = useState([]);
    const [totalPrice, setTotalPrice] = useState(0);


    const removeSeat = (eventId, seatId) => {
        const updatedSeats = cartData[eventId].filter(seatArray => seatArray[0]?.seatId !== seatId);
        updateCart(eventId, updatedSeats);
    };

    const handlePayPalPayment = () => {
        try {
            request('POST', '/api/payment/create', null, {
                    method: 'paypal',
                    amount: totalPrice.toString(),
                    currency: 'PLN',
                    description: 'Purchase of ' + eventName + ' tickets',
                }
            ).then((response) => {
                console.log("response:", response);

                const approvalUrl = response.data.approvalUrl;
                if (approvalUrl) {
                    window.location.href = approvalUrl;
                    console.error('Approval URL not found');
                }
            });
        } catch (error) {
            console.error('Error while initiating payment:', error);
        }
    };



    useEffect(() => {
        if (Object.keys(cartData).length > 0) {
            let sectors = new Set();

            Object.keys(cartData).forEach((eventId) => {
                const eventSeats = cartData[eventId];
                eventSeats.forEach((seatArray) => {
                    const seat = seatArray[0];
                    sectors.add(seat.sectorId);
                });
            });

            const eventId = Object.keys(cartData)[0];

            const params = new URLSearchParams({
                eventId: eventId,
                sectorIds: Array.from(sectors).join(',')
            }).toString();

            request("GET", `/api/cart_data/tickets_info?${params}`, null, null)
                .then((response) => {
                    console.log("response:", response.data);

                    setEventName(response.data.eventName);
                    setEventLocation(response.data.eventLocation);
                    setEventDate(formatDateTime(response.data.eventDateStart));
                    setEventShortDescription(response.data.shortDescription);
                    setSectorsInfo(response.data.sectorsInfo);
                })
                .catch((error) => {
                    console.error('Error while fetching ticket info:', error);
                });

            
        }
    }, [cartData]);


    useEffect(() => {
        const newTotalPrice = Object.keys(cartData).reduce((sum, eventId) => {
            const eventSeats = cartData[eventId];
            console.log("eventSeats -", eventSeats);

            return sum + eventSeats.reduce((eventSum, seatArray) => {
                const seat = seatArray[0];
                const ticketPrice =
                    sectorsInfo
                        .find(sector => sector.sectorId === seat.sectorId)
                        ?.tickets.find(ticket => ticket.ticketPriceId === seat.ticketPriceId)?.price || 0;
                return eventSum + ticketPrice;
            }, 0);
        }, 0);


        setTotalPrice(newTotalPrice);
    }, [cartData, sectorsInfo]); 



    return (
        <Box sx={{ padding: 2, width: '100%' }}>
            <Typography variant="h4" gutterBottom>
                Your Cart
            </Typography>


            {Object.keys(cartData).length === 0 ? (
                <Typography variant="h6" color="textSecondary">
                    Your cart is empty.
                </Typography>
            ) : (
                Object.keys(cartData).map((eventId) => {
                    const eventSeats = cartData[eventId];
                    return (
                        <Box key={eventId} sx={{ marginBottom: 4 }}>
                            <Typography variant="h6" gutterBottom>
                                {eventName} Seats
                            </Typography>
                            <Typography variant="body1" gutterBottom>
                                {eventLocation}: {eventDate}
                            </Typography>
                            {eventShortDescription && (
                                <Typography variant="body2" gutterBottom>
                                    {eventShortDescription}
                                </Typography>
                            )}
                            <TableContainer component={Paper}
                                sx={{ width: '100%', overflowX: 'auto' }}
                            >
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Seat Number</TableCell>
                                            <TableCell>Row Number</TableCell>
                                            <TableCell>Sector</TableCell>
                                            <TableCell>Ticket Type</TableCell>
                                            <TableCell>Ticket Price</TableCell>
                                            <TableCell>Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {eventSeats.map((seatArray) => {
                                            const seat = seatArray[0];
                                            console.log("sectorsInfo -", sectorsInfo);
                                            return (
                                                <TableRow key={seat.seatId}>
                                                    <TableCell sx={{ minWidth: 100 }}>{seat.seatNumber}</TableCell>
                                                    <TableCell sx={{ minWidth: 100 }}>{seat.rowNumber}</TableCell>
                                                    <TableCell sx={{ minWidth: 100 }}>
                                                        {sectorsInfo.find(sector => sector.sectorId === seat.sectorId)?.sectorName || 'Unknown'}
                                                    </TableCell>
                                                    <TableCell sx={{ minWidth: 150 }}>
                                                        <Select
                                                            value={seat.ticketPriceId || ''}
                                                            onChange={(e) => {
                                                                const updatedSeats = eventSeats.map((seatArray) => {
                                                                    if (seatArray[0].seatId === seat.seatId) {
                                                                        seatArray[0].ticketPriceId = e.target.value;
                                                                    }
                                                                    return seatArray;
                                                                });

                                                                if (sessionStorage.getItem('selectedSeatsMap')) {
                                                                    const selectedSeatsMap = JSON.parse(sessionStorage.getItem('selectedSeatsMap')) || {};
                                                                    selectedSeatsMap[eventId] = updatedSeats;
                                                                    sessionStorage.setItem('selectedSeatsMap', JSON.stringify(selectedSeatsMap));
                                                                    
                                                                    updateCart(eventId, selectedSeatsMap[eventId]);
                                                                }
                                                            }}
                                                            sx={{ minWidth: 150 }}
                                                        >
                                                            {sectorsInfo.find(sector => sector.sectorId === seat.sectorId)?.tickets.map((ticket) => (
                                                                <MenuItem key={ticket.ticketPriceId} value={ticket.ticketPriceId}>
                                                                    {ticket.ticketTypeName}
                                                                </MenuItem>
                                                            ))}
                                                        </Select>
                                                    </TableCell>
                                                    <TableCell sx={{ minWidth: 100, fontWeight: "bold" }}>
                                                        {sectorsInfo.find(sector => sector.sectorId === seat.sectorId)?.tickets.find(ticket => ticket.ticketPriceId === seat.ticketPriceId)?.price + ' PLN' || 'Error'}
                                                    </TableCell>
                                                    <TableCell sx={{ minWidth: 100 }}>
                                                        <Button
                                                            variant="contained"
                                                            color="error"
                                                            onClick={() => removeSeat(eventId, seat.seatId)}
                                                        >
                                                            Remove
                                                        </Button>
                                                    </TableCell>
                                                </TableRow>
                                            );
                                        })}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Box>
                    );
                })
            )}

            

            <Box sx={{ marginTop: 2, display: 'flex', justifyContent: 'flex-end' }}>
                <Typography variant="h6">
                    Total Price: {totalPrice} PLN
                </Typography>
            </Box>


            <Box sx={{ marginTop: 2, display: 'flex', justifyContent: 'flex-end' }}>
                <Button
                    variant="contained"
                    sx={{
                        backgroundColor: '#003087',
                        color: '#fff',
                        '&:hover': {
                            backgroundColor: '#001f5b',
                        },
                        display: 'flex',
                        alignItems: 'center',
                    }}
                    onClick={handlePayPalPayment}
                    disabled={Object.keys(cartData).length === 0}
                >
                    Pay with PayPal
                </Button>
            </Box>
        </Box>
    );
};

export default Cart;
