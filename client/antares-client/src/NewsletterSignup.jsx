import React, { useState } from 'react';
import { Box, Button, TextField, Typography, Alert, CircularProgress } from '@mui/material';
import { request } from './helpers/axios_helper';

function NewsletterSignup() {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');
    const [messageType, setMessageType] = useState('success');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailRegex.test(email)) {
            setMessage('Please enter a valid email address.');
            setMessageType('error');
            return;
        }
        
        setIsSubmitting(true);
        setMessage('');

        request("POST", `/api/newsletter/subscribe?email=${email}`)
            .then(async (response) => {
                console.log("Newsletter response:", response);

                if (response.status === 200) {
                    setMessage('Thank you for subscribing!');
                    setMessageType('success');
                } 
            })
            .catch((error) => {
                if (error.response.status === 409) {
                    setMessage('This email is already subscribed.');
                    setMessageType('info');
                    setIsSubmitting(false);
                }
                else {
                    setMessage('An error occurred. Please try again later.');
                    setMessageType('error');
                }
            })
            .finally(() => {
                setIsSubmitting(false);
            });
    };

    return (
        <Box 
            sx={{
                maxWidth: 400,
                mx: 'auto',
                p: 3,
                mt: 5,
                backgroundColor: '#ffffff',
                borderRadius: 2,
                boxShadow: 3,
                textAlign: 'center'
            }}
        >
            <Typography variant="h5" component="h2" gutterBottom>
                Subscribe to Our Newsletter
            </Typography>
            <Typography variant="body2" color="textSecondary" gutterBottom>
                Stay updated with the latest news and exclusive offers.
            </Typography>
            <form onSubmit={handleSubmit}>
                <TextField
                    label="Email Address"
                    variant="outlined"
                    fullWidth
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    sx={{ my: 2 }}
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    fullWidth
                    disabled={isSubmitting}
                    sx={{ py: 1.5 }}
                    startIcon={isSubmitting ? <CircularProgress size={20} color="inherit" /> : null}
                >
                    {isSubmitting ? 'Submitting...' : 'Subscribe'}
                </Button>
            </form>
            {message && (
                <Alert severity={messageType} sx={{ mt: 2 }}>
                    {message}
                </Alert>
            )}
        </Box>
    );
}

export default NewsletterSignup;
