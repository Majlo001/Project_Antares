import React from "react";
import { Card, CardContent, Avatar, Typography, Box } from "@mui/material";
import { serverBaseUrl } from "../helpers/settings";
import { useNavigate } from 'react-router-dom';

const ArtistCard = ({ artistId, artistName, artistImage }) => {
    const navigate = useNavigate();

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            height="100%"
            onClick={() => navigate(`/artists/${artistId}`)}
            sx={{
                cursor: "pointer",
                transition: "transform 0.3s",
                "&:hover .MuiAvatar-root": {
                    transform: "scale(1.1)"
                },
            }}
        >
            <Avatar
                src={serverBaseUrl + artistImage}
                alt={artistName}
                sx={{
                    width: 120,
                    height: 120,
                    marginBottom: 2,
                    transition: "transform 0.3s"
                }}
            />
            <Typography variant="h5" component="div">
                {artistName}
            </Typography>
        </Box>
    );
};

export default ArtistCard;
