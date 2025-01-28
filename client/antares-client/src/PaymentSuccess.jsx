import React, { useEffect, useContext } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { request } from "./helpers/axios_helper";
import { CartContext } from './contexts/CartContext';
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


const PaymentSuccess = () => {
    const { clearCart, cartData } = useContext(CartContext);
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        if (!cartData || Object.keys(cartData).length === 0)
            return;

        const query = new URLSearchParams(location.search);
        const paymentId = query.get('paymentId');
        const token = query.get('token');
        const payerId = query.get('PayerID');

        console.log("cartData:", cartData, Object.keys(cartData));

        if (paymentId && payerId) {
            verifyPayment(paymentId, payerId);
        }
        else {
            console.error('Missing payment parameters.');
        }
    }, [cartData]);

    const verifyPayment = (paymentId, payerId) => {
        try {
            console.log('Verifying payment, cartData:', paymentId, payerId, Object.keys(cartData));
            const seatReservations = Object.keys(cartData).reduce((sum, eventId) => {
                const eventSeats = cartData[eventId];
                console.log('Event seats:', eventSeats);
                return sum.concat(eventSeats.map(item => {
                    return {
                        eventId: eventId,
                        eventSeatStatusId: item[0].seatStatusId,
                        ticketTypeId: item[0].ticketPriceId
                    };
                }));
            }, []);

            console.log('Seat reservations:', seatReservations);

            const paymentRequest = {
                seatReservations: seatReservations,
                paymentMethod: 'paypal',
                discountCode: null
            };

            request('POST', `/api/payment/success`, paymentRequest, {
                paymentId: paymentId,
                payerId: payerId,
            })
            .then((response) => {
                console.log('Payment verification response:', response);

                if (response.data.status === 'Payment approved') {
                    clearCart();
                    // navigate('/');
                } else {
                    alert('Payment verification failed.');
                    // navigate('/');
                }
            })
            
        } catch (error) {
            console.error('Error verifying payment:', error);
            alert('There was an error processing your payment.');
            navigate('/');
        }
    };

    return (
        <Grid sx={{ padding: 3, width: "100%" }}>
            <Typography variant="h4" component="h1" color='error'>
                Payment error!
            </Typography>
            <Typography variant="body1" sx={{ mt: 2, mb: 2 }}>
                There was an error processing your payment on the selected payment method side. <br />
                Your reservation is still active, and you have time to purchase your tickets again.
            </Typography>
        </Grid>
    )

    // return (
    //     <Grid sx={{ padding: 3, width: "100%" }}>
    //         <Typography variant="h4" component="h1">
    //             Payment canceled
    //         </Typography>
    //         <Typography variant="body1" sx={{ mt: 2, mb: 2 }}>
    //             Your payment has been canceled. <br />
    //             Your reservation is still active, and you have time to purchase your tickets again.
    //         </Typography>
    //     </Grid>
    // )

    // return (
    //     <Grid sx={{ padding: 3, width: "100%" }}>
    //         <Typography variant="h4" component="h1">
    //             Payment successful
    //         </Typography>
    //         <Typography variant="body1" sx={{ mt: 2, mb: 2 }}>
    //             Your payment has been successfully processed. Below you can find your tickets.
    //         </Typography>

    //         <Grid container spacing={3}>
    //             <Grid item xs={12}>
    //                 <Grid container spacing={3}>
    //                     <Grid item sm={12} md={6} lg={4} key={1}>
    //                         <Card
    //                             sx={{
    //                                 border: "1px solid #81c784",
    //                                 position: "relative",
    //                             }}
    //                         >
    //                             <Box
    //                                 sx={{
    //                                     position: "absolute",
    //                                     top: 0,
    //                                     right: 0,
    //                                     backgroundColor: "#81c784",
    //                                     color: "#fff",
    //                                     padding: "4px 8px",
    //                                     borderRadius: "0 0 0 8px",
    //                                     display: "flex",
    //                                     alignItems: "center",
    //                                 }}
    //                             ><>
    //                                         <ChairIcon sx={{ mr: 1 }} />
    //                                         <Typography variant="body2">Not Used</Typography>
    //                                     </>
    //                             </Box>
    //                             <Box p={2}>
    //                                 <a 
    //                                     href={"/events/"}
    //                                     target="_blank" 
    //                                     rel="noopener noreferrer" 
    //                                     style={{ textDecoration: "none", color: "inherit" }}
    //                                 >
    //                                     <Typography
    //                                         variant="h6"
    //                                         color="text.primary"
    //                                         p={2}
    //                                         sx = {{ pb: 0 }}
    //                                     >
    //                                         <strong>Rock Warsaw Concert</strong>
    //                                     </Typography>
    //                                 </a>
    //                                 <CardHeader
    //                                     title={"03.02.2025 18:00"}
    //                                     avatar={<EventIcon color="primary" />}
    //                                     sx = {{ pb: 0 }}
    //                                 />
    //                                 <a 
    //                                     href={"/locations/"}
    //                                     target="_blank" 
    //                                     rel="noopener noreferrer" 
    //                                     style={{ textDecoration: "none", color: "inherit" }}
    //                                 >
    //                                     <CardHeader
    //                                         title={"Polish National Stadium - PGE Narodowy"}
    //                                         avatar={<PlaceIcon color="primary" />}
    //                                         sx={{ pb: 0 }}
    //                                     />
    //                                 </a>
    //                                 <CardContent>
    //                                     <Grid container spacing={2} alignItems="start">
    //                                         <Grid item>
    //                                             <EventSeatIcon color="primary"/>
    //                                         </Grid>

    //                                         {/* Kolumna z treścią */}
    //                                         <Grid item xs>
    //                                             <Grid container spacing={1}>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Sector: <strong>Sector 3</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Row: <strong>9</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Seat: <strong>10</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                             </Grid>

    //                                             <Divider sx={{ my: 1 }} />

    //                                             <Grid container spacing={1}>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Price: <strong>50.00 PLN</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Ticket type: <strong>Discounted</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Ticket number: <strong>30</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                             </Grid>
    //                                         </Grid>
    //                                     </Grid>
    //                                 </CardContent>

    //                                 <Divider sx={{ my: 1 }} />

    //                                 <CardActions>
    //                                     <Box sx={{ display: "flex", justifyContent: "flex-end", width: "100%" }}>
    //                                         <Button
    //                                             color="gray"
    //                                             onClick={() => null}
    //                                             target="_blank"
    //                                             startIcon={<LaunchIcon />}
    //                                             p={0}
    //                                             sx={{
    //                                                 minWidth: 0,
    //                                                 '& .MuiButton-startIcon': {
    //                                                     margin: 1,
    //                                                 },
    //                                                 '&:hover': {
    //                                                     color: 'primary.main'
    //                                                 },
    //                                             }}
    //                                         />

    //                                         <Button
    //                                             color="gray"
    //                                             onClick={() => null}
    //                                             target="_blank"
    //                                             download
    //                                             startIcon={<DownloadIcon />}
    //                                             p={0}
    //                                             sx={{
    //                                                 minWidth: 0,
    //                                                 '& .MuiButton-startIcon': {
    //                                                     margin: 1,
    //                                                 },
    //                                                 '&:hover': {
    //                                                     color: 'primary.main'
    //                                                 },
    //                                             }}
    //                                         />
    //                                     </Box>
    //                                 </CardActions>
    //                             </Box>
    //                         </Card>
    //                     </Grid>


    //                     <Grid item sm={12} md={6} lg={4} key={2}>
    //                         <Card
    //                             sx={{
    //                                 border: "1px solid #81c784",
    //                                 position: "relative",
    //                             }}
    //                         >
    //                             <Box
    //                                 sx={{
    //                                     position: "absolute",
    //                                     top: 0,
    //                                     right: 0,
    //                                     backgroundColor: "#81c784",
    //                                     color: "#fff",
    //                                     padding: "4px 8px",
    //                                     borderRadius: "0 0 0 8px",
    //                                     display: "flex",
    //                                     alignItems: "center",
    //                                 }}
    //                             ><>
    //                                         <ChairIcon sx={{ mr: 1 }} />
    //                                         <Typography variant="body2">Not Used</Typography>
    //                                     </>
    //                             </Box>
    //                             <Box p={2}>
    //                                 <a 
    //                                     href={"/events/"}
    //                                     target="_blank" 
    //                                     rel="noopener noreferrer" 
    //                                     style={{ textDecoration: "none", color: "inherit" }}
    //                                 >
    //                                     <Typography
    //                                         variant="h6"
    //                                         color="text.primary"
    //                                         p={2}
    //                                         sx = {{ pb: 0 }}
    //                                     >
    //                                         <strong>Rock Warsaw Concert</strong>
    //                                     </Typography>
    //                                 </a>
    //                                 <CardHeader
    //                                     title={"03.02.2025 18:00"}
    //                                     avatar={<EventIcon color="primary" />}
    //                                     sx = {{ pb: 0 }}
    //                                 />
    //                                 <a 
    //                                     href={"/locations/"}
    //                                     target="_blank" 
    //                                     rel="noopener noreferrer" 
    //                                     style={{ textDecoration: "none", color: "inherit" }}
    //                                 >
    //                                     <CardHeader
    //                                         title={"Polish National Stadium - PGE Narodowy"}
    //                                         avatar={<PlaceIcon color="primary" />}
    //                                         sx={{ pb: 0 }}
    //                                     />
    //                                 </a>
    //                                 <CardContent>
    //                                     <Grid container spacing={2} alignItems="start">
    //                                         <Grid item>
    //                                             <EventSeatIcon color="primary"/>
    //                                         </Grid>

    //                                         {/* Kolumna z treścią */}
    //                                         <Grid item xs>
    //                                             <Grid container spacing={1}>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Sector: <strong>Sector 3</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Row: <strong>9</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Seat: <strong>11</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                             </Grid>

    //                                             <Divider sx={{ my: 1 }} />

    //                                             <Grid container spacing={1}>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Price: <strong>60.00 PLN</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Ticket type: <strong>Normal</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                                 <Grid item xs={12}>
    //                                                     <Typography variant="body2">
    //                                                         Ticket number: <strong>31</strong>
    //                                                     </Typography>
    //                                                 </Grid>
    //                                             </Grid>
    //                                         </Grid>
    //                                     </Grid>
    //                                 </CardContent>

    //                                 <Divider sx={{ my: 1 }} />

    //                                 <CardActions>
    //                                     <Box sx={{ display: "flex", justifyContent: "flex-end", width: "100%" }}>
    //                                         <Button
    //                                             color="gray"
    //                                             onClick={() => null}
    //                                             target="_blank"
    //                                             startIcon={<LaunchIcon />}
    //                                             p={0}
    //                                             sx={{
    //                                                 minWidth: 0,
    //                                                 '& .MuiButton-startIcon': {
    //                                                     margin: 1,
    //                                                 },
    //                                                 '&:hover': {
    //                                                     color: 'primary.main'
    //                                                 },
    //                                             }}
    //                                         />

    //                                         <Button
    //                                             color="gray"
    //                                             onClick={() => null}
    //                                             target="_blank"
    //                                             download
    //                                             startIcon={<DownloadIcon />}
    //                                             p={0}
    //                                             sx={{
    //                                                 minWidth: 0,
    //                                                 '& .MuiButton-startIcon': {
    //                                                     margin: 1,
    //                                                 },
    //                                                 '&:hover': {
    //                                                     color: 'primary.main'
    //                                                 },
    //                                             }}
    //                                         />
    //                                     </Box>
    //                                 </CardActions>
    //                             </Box>
    //                         </Card>
    //                     </Grid>
    //                 </Grid>
    //             </Grid>
    //         </Grid>
    //     </Grid>
    // );
};

export default PaymentSuccess;
