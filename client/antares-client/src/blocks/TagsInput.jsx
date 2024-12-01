import React, { useState, useEffect, useImperativeHandle, forwardRef } from "react";
import { Autocomplete, Chip, Box, TextField } from "@mui/material";
import { request } from "../helpers/axios_helper";

const TagsInput = forwardRef(({ onTagsChange }, ref) => {
    const [tags, setTags] = useState([]);
    const [inputValue, setInputValue] = useState("");
    const [tagsOptions, setTagsOptions] = useState([]);

    const addTagsList = (tagsList) => {
        const updatedTags = [...tagsList];
        setTags(updatedTags);
        onTagsChange(updatedTags);
    }
    
    useImperativeHandle(ref, () => ({
        addTags: addTagsList,
    }));

    const fetchTags = (query) => {
        if (query.length < 1) {
            setTagsOptions([]);
            return;
        }

        return request('GET', `/api/dicts/event_tags?query=${query}`)
            .then(response => {
                setTagsOptions(response.data);
                return response.data;
            })
            .catch (error => {
                console.error("Błąd podczas pobierania tagów:", error);
                // showSnackbar("Błąd podczas pobierania lokalizacji: " + error.message, 'error');
                throw error;
            });
    };

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            fetchTags(inputValue);
        }, 300);

        return () => clearTimeout(delayDebounceFn);
    }, [inputValue]);

    const handleAddTag = (e) => {
        if (e.key === "Enter" && inputValue.trim() !== "") {
            e.preventDefault();
            if (!tags.includes(inputValue.trim())) {
                setTags([...tags, inputValue.trim()]);
                onTagsChange([...tags, inputValue.trim()]);
            }
            setInputValue("");
        }
    };

    const handleDeleteTag = (tagToDelete) => {
        setTags(tags.filter((tag) => tag !== tagToDelete));
        onTagsChange(tags.filter((tag) => tag !== tagToDelete));
    };

    const handleAutocompleteChange = (event, newValue) => {
        if (newValue && !tags.includes(newValue)) {
            const updatedTags = [...tags, newValue];
            setTags(updatedTags);
            onTagsChange(updatedTags);
        }
        setInputValue("");
    };

    return (
        <Box
            onSubmit={(e) => e.preventDefault()}
            sx={{ display: "flex", flexDirection: "column", gap: 2, mb: 2 }}
        >
            <Autocomplete
                freeSolo
                options={tagsOptions.map((option) => option.name)} 
                inputValue={inputValue}
                onInputChange={(event, newInputValue) => setInputValue(newInputValue)}
                onChange={handleAutocompleteChange}
                renderInput={(params) => (
                    <TextField
                        {...params}
                        label="Add a tag"
                        placeholder="Type and press Enter"
                        onKeyDown={handleAddTag}
                    />
                )}
            />
            <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1 }}>
                {tags.map((tag, index) => (
                    <Chip
                        key={index}
                        label={tag}
                        onDelete={() => handleDeleteTag(tag)}
                        color="primary"
                        variant="outlined"
                    />
                ))}
            </Box>
        </Box>
    );
});

export default TagsInput;
