import React, { useEffect, useContext } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { request } from "./helpers/axios_helper";
import { CartContext } from './contexts/CartContext';

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
        <div>
            <h1>Processing Payment...</h1>
            <p>Please wait while we verify your payment.</p>
        </div>
    );
};

export default PaymentSuccess;
