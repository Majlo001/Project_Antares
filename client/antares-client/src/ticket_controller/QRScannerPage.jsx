import React, { useEffect, useRef, useState } from "react";
import QrScanner from "qr-scanner";
import { Box, Typography, Button } from "@mui/material";
import { request } from "../helpers/axios_helper";
import { formatDateTime } from "../helpers/time_format_helper";

const QRScannerPage = () => {
    const videoRef = useRef(null);
    const [scannedData, setScannedData] = useState(null);
    const [error, setError] = useState(null);

    const [ticketId, setTicketId] = useState(null);
    const [eventName, setEventName] = useState("");
    const [eventLocation, setEventLocation] = useState("");
    const [eventDate, setEventDate] = useState(null);
    const [sectorName, setSectorName] = useState("");
    const [rowNumber, setRowNumber] = useState(null);
    const [seatNumber, setSeatNumber] = useState(null);
    const [ticketTypeName, setTicketTypeName] = useState("");


    const handleTicketValidation = (ticketCode) => {
        request("GET", `/api/tickets/validate/${ticketCode}`)
            .then((response) => {
                console.log("Tickets fetched:", response.data);
                setTicketId(response.data.ticketId || null);
                setEventName(response.data.eventName || "");
                setEventLocation(response.data.eventLocation || "");
                setEventDate(response.data.eventDate ? formatDateTime(response.data.eventDate) : null);
                setSectorName(response.data.sectorName || "");
                setRowNumber(response.data.rowNumber || null);
                setSeatNumber(response.data.seatNumber || null);
                setTicketTypeName(response.data.ticketTypeName || "");
                alert("event: ", JSON.stringify(response.data));

            })
            .catch((error) => {
                console.error("Error fetching tickets:", error);
                setError("Error fetching tickets:", error.message);
            })
    }

    useEffect(() => {
        const videoElement = videoRef.current;

        const qrScanner = new QrScanner(
            videoElement,
            (result) => {
                setScannedData(result.data);
                console.log("Scanned Data:", JSON.stringify(result.data));
                alert(`Scanned Data: ${JSON.stringify(result.data)}`);
                handleTicketValidation(result.data);

                // qrScanner.stop();
            },
            {
                onDecodeError: (error) => {
                    console.error("Decode error:", error);
                    // setError("Nie udało się zeskanować kodu.");
                },
                highlightScanRegion: true,
            }
        );

        qrScanner.start().catch((err) => {
            console.error("Camera error:", err);
            setError("Brak dostępu do kamery lub inny błąd.");
        });

        return () => {
            qrScanner.destroy();
        };
    }, []);

    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                height: "100%",
                width: "100%",
                textAlign: "center",
                p: 4,
            }}
        >
            <Typography variant="h5" gutterBottom>
                QR Scanner
            </Typography>

            <video
                ref={videoRef}
                style={{
                    width: "100%",
                    maxWidth: "400px",
                    borderRadius: "8px",
                    border: "2px solid #ccc",
                }}
            ></video>

            {scannedData && (
                <Box mt={2}>
                    <Typography variant="body1">
                        Ticket ID: {ticketId}
                    </Typography>
                    <Typography variant="body1">
                        Event Name: {eventName}
                    </Typography>
                    <Typography variant="body1">
                        Event Location: {eventLocation}
                    </Typography>
                    <Typography variant="body1">
                        Event Date: {eventDate}
                    </Typography>
                    <Typography variant="body1">
                        Sector Name: {sectorName}
                    </Typography>
                    <Typography variant="body1">
                        Row Number: {rowNumber}
                    </Typography>
                    <Typography variant="body1">
                        Seat Number: {seatNumber}
                    </Typography>
                    <Typography variant="body1">
                        Ticket Type: {ticketTypeName}
                    </Typography>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={() => {
                            setScannedData(null)
                        }}
                        sx={{ mt: 2 }}
                    >
                        Scan Again
                    </Button>
                </Box>
            )}

            {error && (
                <Typography variant="body2" color="error" mt={2}>
                    {error}
                </Typography>
            )}
        </Box>
    );
};

export default QRScannerPage;
