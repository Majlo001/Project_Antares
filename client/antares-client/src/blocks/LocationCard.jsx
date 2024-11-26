import React from "react";
import { Card, CardContent, CardMedia, Typography, Link, Box } from "@mui/material";
import { serverBaseUrl } from "../helpers/settings";
import { useNavigate } from "react-router-dom";


const LocationCard = ({locationId, locationName, locationAddress, locationCity, locationCountry, locationImage, mapLink }) => {
    
    const navigate = useNavigate();
    
    return (
        <Card
            onClick={() => navigate(`/locations/${locationId}`)}
            sx={{
                display: 'flex',
                padding: 2,
                cursor: "pointer",
                transition: "transform 0.3s",
                "&:hover .MuiCardMedia-root": {
                    transform: "scale(1.05)"
                },
            }}
        >
            <Box sx={{ flex: 1, overflow: "hidden", borderRadius: "8px" }}>
                <CardMedia
                    component="img"
                    image={serverBaseUrl + locationImage}
                    alt={locationName}
                    sx={{
                        width: "100%",
                        height: "auto",
                        transition: "transform 0.3s",
                        borderRadius: "8px"
                    }}
                />
            </Box>
            <Box 
                sx={{
                    flex: 2,
                    marginLeft: 2
                }}
            >
                <CardContent>
                    <Typography variant="h5" component="div">
                        {locationName}
                    </Typography>
                    <Typography variant="body1" color="text.primary" sx={{ marginBottom: 1 }}>
                        {locationCity}, {locationCountry}
                    </Typography>
                    <Typography variant="body2" color="text.primary" sx={{ marginBottom: 2 }}>
                        {locationAddress}
                    </Typography>
                    <Typography>
                        <Link href={mapLink} target="_blank" underline="hover" sx={{ color: "primary.main" }}>
                            Show on map
                        </Link>
                    </Typography>
                </CardContent>
            </Box>
        </Card>
    );
};

export default LocationCard;
