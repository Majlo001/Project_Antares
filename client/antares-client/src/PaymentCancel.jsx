import React from 'react';
import { useNavigate } from 'react-router-dom';

const PaymentCancel = () => {
    const navigate = useNavigate();

    return (
        <div>
            <h1>Payment Cancelled</h1>
            <p>You have cancelled the payment process.</p>
            <button onClick={() => navigate('/')}>Go to Home</button>
        </div>
    );
};

export default PaymentCancel;
