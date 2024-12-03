import React, { useEffect, useRef, useState } from "react";
import QrScanner from "qr-scanner";
import { Box, Typography, Button } from "@mui/material";

const QRScannerPage = () => {
    const videoRef = useRef(null); // Referencja do elementu <video>
    const [scannedData, setScannedData] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const videoElement = videoRef.current;

        const qrScanner = new QrScanner(
            videoElement,
            (result) => {
                setScannedData(result); // Zapisujemy zeskanowane dane
                console.log("Scanned Data:", result);

                // Opcjonalnie zatrzymujemy skaner po zeskanowaniu
                qrScanner.stop();
            },
            {
                onDecodeError: (error) => {
                    console.error("Decode error:", error);
                    setError("Nie udało się zeskanować kodu.");
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
                <Typography variant="body1" mt={2}>
                    Zeskanowane dane: <strong>{scannedData}</strong>
                </Typography>
            )}

            {error && (
                <Typography variant="body2" color="error" mt={2}>
                    {error}
                </Typography>
            )}

            {scannedData && (
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => window.location.reload()}
                    sx={{ mt: 2 }}
                >
                    Zeskanuj ponownie
                </Button>
            )}
        </Box>
    );
};

export default QRScannerPage;
