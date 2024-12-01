import React, { useState, useEffect, useImperativeHandle, forwardRef } from "react";
import { Autocomplete, Chip, Box, TextField } from "@mui/material";
import { request } from "../helpers/axios_helper";

const ArtistsInput = forwardRef(({ onArtistsChange }, ref) => {
    const [selectedArtists, setSelectedArtists] = useState([]);
    const [artistOptions, setArtistOptions] = useState([]);
    const [inputValue, setInputValue] = useState("");

    const fetchArtists = (query) => {
        if (query.length < 1) {
            setArtistOptions([]);
            return;
        }

        return request('GET', `/api/dicts/event_artists?query=${query}`)
            .then(response => {
                setArtistOptions(response.data);
            })
            .catch(error => {
                console.error("Błąd podczas pobierania artystów:", error);
                throw error;
            });
    };

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            fetchArtists(inputValue);
        }, 300);

        return () => clearTimeout(delayDebounceFn);
    }, [inputValue]);

    const addArtistsByIds = (artistIds) => {
        const fetchArtistById = (id) => {
            return request('GET', `/api/dicts/event_artists/${id}`)
                .then(response => response.data)
                .catch(error => {
                    console.error(`Błąd podczas pobierania artysty o ID ${id}:`, error);
                    throw error;
                });
        };

        Promise.all(artistIds.map(fetchArtistById))
            .then(fetchedArtists => {
                const updatedArtists = [...selectedArtists, ...fetchedArtists];
                setSelectedArtists(updatedArtists);
                onArtistsChange(updatedArtists);
            })
            .catch(error => {
                console.error("Błąd podczas dodawania artystów:", error);
            });
    };

    useImperativeHandle(ref, () => ({
        addArtists: addArtistsByIds,
    }));

    const handleAddArtist = (event, newArtist) => {
        if (newArtist && !selectedArtists.some((artist) => artist.id === newArtist.id)) {
            const updatedArtists = [...selectedArtists, newArtist];
            setSelectedArtists(updatedArtists);
            onArtistsChange(updatedArtists);
        }
        setInputValue(""); 
    };

    const handleDeleteArtist = (artistToDelete) => {
        const updatedArtists = selectedArtists.filter(
            (artist) => artist.id !== artistToDelete.id
        );
        setSelectedArtists(updatedArtists);
        onArtistsChange(updatedArtists);
    };

    return (
        <Box sx={{ display: "flex", flexDirection: "column", gap: 2, mb: 2 }}>
            <Autocomplete
                options={artistOptions}
                getOptionLabel={(option) => option.name}
                inputValue={inputValue}
                onInputChange={(event, newInputValue) => setInputValue(newInputValue)}
                onChange={handleAddArtist}
                noOptionsText="Type to see options"
                renderInput={(params) => (
                    <TextField
                        {...params}
                        label="Wybierz artystę"
                        placeholder="Wpisz nazwę artysty"
                    />
                )}
            />
            <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1 }}>
                {selectedArtists.map((artist) => (
                    <Chip
                        key={artist.id}
                        label={artist.name}
                        onDelete={() => handleDeleteArtist(artist)}
                        color="primary"
                        variant="outlined"
                    />
                ))}
            </Box>
        </Box>
    );
});

export default ArtistsInput;
