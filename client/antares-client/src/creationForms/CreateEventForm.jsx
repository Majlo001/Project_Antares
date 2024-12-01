import { useState, useEffect, useRef } from 'react';
import { Box, TextField, Button, Typography, Autocomplete, Checkbox, FormControlLabel, Paper, Snackbar, Alert, CircularProgress, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFnsV3';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { useNavigate, useParams } from 'react-router-dom';
import { request } from './../helpers/axios_helper';
import { getFullDateFormArray } from './../helpers/time_format_helper';
import { serverBaseUrl } from './../helpers/settings';

import TagsInput from './../blocks/TagsInput';
import ArtistsInput from './../blocks/ArtistsInput';
// import { is } from 'date-fns/locale';
// import { set } from 'date-fns';



const CreateEventForm = () => {
    const navigate = useNavigate();
    const { eventId } = useParams();

    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [shortDescription, setShortDescription] = useState('');
    const [isPublic, setIsPublic] = useState(false);
    const [isSingleEvent, setIsSingleEvent] = useState(false);
    const [eventStatus, setEventStatus] = useState(null);
    const [eventSeries, setEventSeries] = useState(null);
    const [location, setLocation] = useState(null);
    const [locationVariant, setLocationVariant] = useState(null);
    const [eventDateStart, setEventDateStart] = useState(null);
    const [eventDateEnd, setEventDateEnd] = useState(null);
    const [ticketPurchaseDateStart, setTicketPurchaseDateStart] = useState(null);
    const [ticketPurchaseDateEnd, setTicketPurchaseDateEnd] = useState(null);
    const [maxReservationsPerUser, setMaxReservationsPerUser] = useState('');
    const [forceChoosingWithoutBreaks, setForceChoosingWithoutBreaks] = useState(false);
    const [mainImage, setMainImage] = useState(null);
    const [locationOptions, setLocationOptions] = useState([]);
    const [locationVariantOptions, setLocationVariantOptions] = useState([]);
    const [eventStatusOptions, setEventStatusOptions] = useState([]);
    const [eventSeriesOptions, setEventSeriesOptions] = useState([]);
    const [imagePreview, setImagePreview] = useState(null);
    const [inputLocationValue, setInputLocationValue] = useState('');
    const [isEventSeatStatusesCreated, setIsEventSeatStatusesCreated] = useState(false);


    
    const artistsInputRef = useRef();
    const tagsInputRef = useRef();
    const [eventCategoriesOptions, setEventCategoriesOptions] = useState([]);
    const [eventSeriesCategory, setEventSeriesCategory] = useState(null);
    const [eventSeriesId, setEventSeriesId] = useState(null);
    const [youtubePreviewUrl, setYoutubePreviewUrl] = useState('');

    const [eventTags, setEventTags] = useState([]);
    const [artistsIds, setArtistsIds] = useState([]);
    const [eventSeriesDescription, setEventSeriesDescription] = useState('');
    const [eventSeriesShortDescription, setEventSeriesShortDescription] = useState('');
    const [loading, setLoading] = useState(false); 
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');
    const [snackbarMessage, setSnackbarMessage] = useState('');

    const [openModal, setOpenModal] = useState(false);
    const [createdEventId, setCreatedEventId] = useState(null);

    const handleImageUpload = (e) => {
        const file = e.target.files[0];
        const formData = new FormData();
        formData.append("file", file);

        request("POST", "/api/images/upload", formData).then(response => {
            console.log("Uploaded image:", response.data);
            if (response.data) {
                setMainImage(response.data);
                const previewUrl = URL.createObjectURL(file);
                setImagePreview(previewUrl);
            }
        }).catch(error => {
            console.error("Error uploading image:", error);
            showSnackbar("Error uploading image: " + error.message, 'error');
        });
    };

    const fetchSingleEventSeries = (eventSeriesId, categories) => {
        request('GET', `/api/eventseries/single_event_series/${eventSeriesId}`)
            .then(response => {
                const eventSeries = response.data;
                console.log("eventSeries.eventCategory", eventSeries.eventCategoryId);

                setEventSeriesDescription(eventSeries.description || "");
                setEventSeriesShortDescription(eventSeries.shortDescription || "");
                setYoutubePreviewUrl(eventSeries.youtubePreviewUrl || "");
                setEventTags(eventSeries.eventTags || []);
                setArtistsIds(eventSeries.artistsIds || []);

                const category = categories.find(category => category.id === eventSeries.eventCategoryId);
                setEventSeriesCategory(category|| null);

                
                if (artistsInputRef.current) {
                    artistsInputRef.current.addArtists(eventSeries.artistsIds);
                }

                console.log("eventSeries.eventTags", eventSeries.eventTags);
                if (tagsInputRef.current) {
                    tagsInputRef.current.addTags(eventSeries.eventTags);
                }

            })
            .catch(error => {
                console.error("Error fetching single event series:", error);
                showSnackbar("Error fetching single event series: " + error.message, 'error');
            });

    }

    const fetchEvent = (eventId, statuses, serieses, categories) => {
        request('GET', `/api/events/owner_detail/${eventId}`)
            .then(response => {
                const event = response.data;
                console.log("Fetched event:", event);
                setName(event.name);
                setDescription(event.description);
                setShortDescription(event.shortDescription);
                setIsPublic(event.isPublic);
                setIsSingleEvent(event.singleEvent);

                if (statuses.length > 0) {
                    const status = statuses.find(status => status.id === event.eventStatusId);
                    setEventStatus(status || null);
                }
                else {
                    console.warn("No event status options available");
                }

                if (serieses.length > 0) {
                    const series = serieses.find(series => series.id === event.eventStatusId);
                    setEventSeries(series || null);
                }
                else {
                    console.warn("No event status options available");
                }
                
                Promise.all([fetchLocations(event.locationName)])
                .then(([location]) => {
                    if (location.length > 0) {
                        const loc = location.find(location => location.id === event.locationId);
                        setInputLocationValue(loc?.name || '');
                        setLocation(loc || null);
                    }
                })

                Promise.all([fetchLocationVariants(event.locationId)])
                .then(([locationVariant]) => {
                    if (locationVariant.length > 0) {
                        const variant = locationVariant.find(variant => variant.id === event.locationVariantId);
                        setLocationVariant(variant || null);
                    }
                })

                if (event.eventDateStart)
                    setEventDateStart(getFullDateFormArray(event.eventDateStart) || null);
                if (event.eventDateEnd)
                    setEventDateEnd(getFullDateFormArray(event.eventDateEnd) || null);
                if (event.ticketPurchaseDateStart)
                    setTicketPurchaseDateStart(getFullDateFormArray(event.ticketPurchaseDateStart) || null);
                if (event.ticketPurchaseDateEnd)
                    setTicketPurchaseDateEnd(getFullDateFormArray(event.ticketPurchaseDateEnd) || null);
                
                setMaxReservationsPerUser(event.maxReservationsPerUser || null);
                setForceChoosingWithoutBreaks(event.forceChoosingWithoutBreaks || null);
                setMainImage(event.mainImage || null);
                if (event.mainImage)
                    setImagePreview(serverBaseUrl + event.mainImage);

                setIsEventSeatStatusesCreated(event.isEventSeatStatusesCreated || false);

                if (event.singleEvent) {
                    fetchSingleEventSeries(event.eventSeriesId, categories);
                }

            })
            .catch(error => {
                console.error("Error fetching event:", error);
                showSnackbar("Error fetching event: " + error.message, 'error');
            });
        
    };

    const fetchLocations = (query) => {
        if (query.length < 1) return;

        return request('GET', `/api/dicts/locations?query=${query}`)
            .then(response => {
                setLocationOptions(response.data);
                return response.data;
            })
            .catch (error => {
                console.error("Błąd podczas pobierania lokalizacji:", error);
                showSnackbar("Błąd podczas pobierania lokalizacji: " + error.message, 'error');
                throw error;
            });
    };

    const fetchLocationVariants = (locationId) => {
        return request('GET', `/api/dicts/location_variants?id=${locationId}`)
            .then(response => {
                setLocationVariantOptions(response.data);
                return response.data;
            })
            .catch(error => {
                console.error("Błąd podczas pobierania wariantów lokalizacji:", error);
                showSnackbar("Błąd podczas pobierania wariantów lokalizacji: " + error.message, 'error');
                throw error;
            });
    };

    const fetchStatuses = () => {
        return request('GET', `/api/dicts/event_statuses`)
            .then(response => {
                setEventStatusOptions(response.data);
                return response.data;
            })
            .catch(error => {
                console.error("Błąd podczas pobierania statusów wydarzeń:", error);
                showSnackbar("Błąd podczas pobierania statusów wydarzeń: " + error.message, 'error');
                throw error;
            });
    };

    const fetchEventSeriesDict = () => {
        return request('GET', `/api/dicts/event_series`)
            .then(response => {
                setEventSeriesOptions(response.data);
                return response.data;
            })
            .catch(error => {
                console.error("Błąd podczas pobierania serii wydarzeń:", error);
                showSnackbar("Błąd podczas pobierania serii wydarzeń: " + error.message, 'error');
                throw error;
            });
    };

    const fetchCategories = () => {
        return request('GET', `/api/dicts/event_categories`)
            .then(response => {
                setEventCategoriesOptions(response.data);
                return response.data;
            })
            .catch(error => {
                console.error("Błąd podczas pobierania kategorii wydarzeń:", error);
                showSnackbar("Błąd podczas pobierania kategorii wydarzeń: " + error.message, 'error');
                throw error;
            });
    };  

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            fetchLocations(inputLocationValue);
        }, 300);

        return () => clearTimeout(delayDebounceFn);
    }, [inputLocationValue]);

    
    useEffect(() => {
        setLoading(true);

        Promise.all([fetchStatuses(), fetchEventSeriesDict(), fetchCategories()])
        .then(([statuses, series, categories]) => {
            if (eventId) {
                fetchEvent(eventId, statuses, series, categories);
            }
        })
        .catch(error => {
            console.error("Error fetching initial data:", error);
            showSnackbar("Error fetching initial data: " + error.message, 'error');
        })
        .finally(() => {
            setLoading(false);
        });
    }, []);

    const showSnackbar = (message, severity) => {
        setSnackbarMessage(message);
        setSnackbarSeverity(severity);
        setSnackbarOpen(true);
    };

    const handleCloseSnackbar = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setSnackbarOpen(false);
    };



    const createOrUpdateEventSeries = () => {
        return new Promise((resolve, reject) => {
            const singleEventSeries = {
                name,
                youtubePreviewUrl,
                shortDescription: eventSeriesShortDescription,
                description: eventSeriesDescription,
                eventCategoryId: eventSeriesCategory?.id,
                eventTags,
                artistsIds
            }


            if (isSingleEvent) {
                if (!eventSeriesCategory) {
                    showSnackbar("Select event category", 'error');
                    return;
                }

                if (eventId) {
                    request("POST", `/api/eventseries/edit_single/${eventId}`, singleEventSeries)
                        .then((response) => {
                            console.log("Zedytowano serie wydarzeń, response:", response)
                            resolve(eventSeriesId);
                        })
                        .catch(error => {
                            console.error("Błąd przy edycji serii wydarzeń:", error)
                            showSnackbar("Błąd przy edycji serii wydarzeń: " + error.message, 'error');
                            reject(error);
                        });
                } 
                else {
                    request("POST", "/api/eventseries/create_single", singleEventSeries)
                        .then((response) => {
                            console.log("Utworzono serie wydarzeń, response:", response)
                            setEventSeriesId(response.data.id);
                            resolve(response.data.id);
                        })
                        .catch(error => {
                            console.error("Błąd przy tworzeniu serii wydarzeń:", error)
                            showSnackbar("Błąd przy tworzeniu serii wydarzeń: " + error.message, 'error');
                            reject(error);
                        });
                }
            }
            else {
                resolve(null);
            }
        });
    };


    const handleSubmit = (e) => {
        e.preventDefault();

        Promise.all([createOrUpdateEventSeries()])
            .then(([eventSeriesId]) => {
                console.log("eventSeriesId", eventSeriesId)

                const event = {
                    name,
                    isPublic,
                    singleEvent: isSingleEvent,
                    description,
                    shortDescription,
                    eventStatusId: eventStatus?.id,
                    eventSeriesId: eventSeriesId || eventSeries?.id,
                    locationId: location?.id,
                    locationVariantId: locationVariant?.id,
                    eventDateStart,
                    eventDateEnd,
                    ticketPurchaseDateStart,
                    ticketPurchaseDateEnd,
                    maxReservationsPerUser: parseInt(maxReservationsPerUser, 10),
                    forceChoosingWithoutBreaks,
                    mainImage
                };

                if (eventId) {
                    request("POST", `/api/events/edit/${eventId}`, event)
                        .then((response) => {
                            console.log("Zedytowano wydarzenie, response:", response)
                            setCreatedEventId(response.data.id)
                            setOpenModal(true)
                        })
                        .catch(error => {
                            console.error("Błąd przy edycji wydarzenia:", error)
                            showSnackbar("Błąd przy edycji wydarzenia: " + error.message, 'error');
                        });
                }
                else {
                    request("POST", "/api/events/create", event)
                        .then((response) => {
                            console.log("Utworzono wydarzenie, response:", response)
                            setCreatedEventId(response.data.id)
                            setOpenModal(true)
                        })
                        .catch(error => {
                            console.error("Błąd przy tworzeniu wydarzenia:", error)
                            showSnackbar("Błąd przy tworzeniu wydarzenia: " + error.message, 'error');
                        });
                }
            })
            .catch(error => {
                console.error("Error creating or updating event series:", error);
                showSnackbar("Error creating or updating event series: " + error.message, 'error');
            });
    };

    useEffect(() => {
        if (eventDateEnd !== null && ticketPurchaseDateEnd === null) {
            setTicketPurchaseDateEnd(eventDateEnd);
        }
    }, [eventDateEnd]);



    const handleTagsChange = (updatedTags) => {
        setEventTags(updatedTags);
    };

    const handleArtistsChange = (updatedArtists) => {
        setArtistsIds(updatedArtists.map(artist => artist.id));
    };


    const handleNavigateToEvent = () => {
        if (createdEventId) {
            navigate(`/events/${createdEventId}`);
        }
        setOpenModal(false);
    };

    const handleCloseModal = () => {
        setOpenModal(false);
    };

    return (
        <>
            {loading && (
                <CircularProgress sx={{ display: 'block', margin: '16px auto' }} />
            )}
            <Paper elevation={3} sx={{ maxWidth: 1000, mx: 'auto', mt: 4, mb:4, p: 4, width: '100%' }}>
                <Typography variant="h4" component="h1" gutterBottom mb={4}>{eventId ? 'Edit event' : 'Create new event'} </Typography>
                <Box component="form" onSubmit={handleSubmit}>
                    <TextField
                        label="Event name"
                        fullWidth
                        required
                        value={name}
                        onChange={e => setName(e.target.value)}
                        sx={{ mb: 2 }}
                    />

                    <Box sx={{ mb: 2 }}>
                        <Button variant="contained" component="label" sx={{ mb: 2 }}>
                            {mainImage ? 'Change main image' : 'Upload main image'}
                            <input type="file" hidden onChange={handleImageUpload} />
                        </Button>

                        {imagePreview && (
                            <img
                                src={imagePreview}
                                alt="Image preview"
                                style={{ maxWidth: '100%', maxHeight: '400px', marginTop: '16px' }}
                            />
                        )}
                    </Box>
                    
                    <FormControlLabel
                        sx={{ mb: 2 }}
                        control={<Checkbox
                            checked={isPublic}
                            onChange={e => setIsPublic(e.target.checked)}
                        />}
                        label="Public event"
                    />

                    <TextField
                        label="Short Description"
                        fullWidth
                        multiline
                        rows={4}
                        value={shortDescription}
                        onChange={e => setShortDescription(e.target.value)}
                        sx={{ mb: 2 }}
                        inputProps={{ maxLength: 200 }}
                        helperText={`${shortDescription.length}/200`} 
                    />
                    
                    <ReactQuill
                        value={description}
                        onChange={setDescription}
                        placeholder="Enter description of the event"
                        style={{ marginBottom: '56px', height: '260px' }}
                        modules={{
                            toolbar: [
                                [{ 'header': '1' }, { 'header': '2' }, { 'header': '3' }, { 'header': '4' }, { 'font': [] }],
                                [{ 'size': [] }],
                                ['bold', 'italic', 'underline', 'strike', 'blockquote'],
                                [{ 'list': 'ordered'}, { 'list': 'bullet' }],
                                ['link', 'image'],
                                ['clean']
                            ],
                        }}
                    />

                    <Autocomplete
                        options={eventStatusOptions}
                        getOptionLabel={(option) => option.name || ''}
                        value={eventStatus || null}
                        onChange={(e, value) => setEventStatus(value)}
                        renderInput={(params) => (
                            <TextField
                                {...params}
                                required
                                label="Event status"
                                />
                            )}
                        sx={{ mb: 2 }}
                    />
                    
                    <FormControlLabel
                        sx={{ mb: 2 }}
                        control={<Checkbox
                            checked={isSingleEvent}
                            onChange={e => setIsSingleEvent(e.target.checked)}
                        />}
                        label="Single event"
                        disabled={!!eventId}
                    />

                    {isSingleEvent ? (
                        <Paper sx={{ p: 2, mb: 2 }}>
                            <Typography variant="h6" gutterBottom mb={2}>
                                Single Event Form
                            </Typography>

                            <TextField
                                label="YouTube Preview URL"
                                fullWidth
                                value={youtubePreviewUrl}
                                onChange={e => setYoutubePreviewUrl(e.target.value)}
                                sx={{ mb: 2 }}
                            />

                            <Autocomplete
                                options={eventCategoriesOptions}
                                getOptionLabel={(option) => option.name}
                                value={eventSeriesCategory}
                                onChange={(e, value) => setEventSeriesCategory(value)}
                                renderInput={(params) => <TextField {...params} label="Event categories" />}
                                sx={{ mb: 2 }}
                            />

                            <TextField
                                label="Short Description"
                                fullWidth
                                multiline
                                rows={4}
                                value={eventSeriesShortDescription}
                                onChange={e => setEventSeriesShortDescription(e.target.value)}
                                sx={{ mb: 2 }}
                                inputProps={{ maxLength: 200 }}
                                helperText={`${eventSeriesShortDescription.length}/200`} 
                            />
                            
                            <ReactQuill
                                value={eventSeriesDescription}
                                onChange={setEventSeriesDescription}
                                placeholder="Enter description of the event"
                                style={{ marginBottom: '56px', height: '260px' }}
                                modules={{
                                    toolbar: [
                                        [{ 'header': '1' }, { 'header': '2' }, { 'header': '3' }, { 'header': '4' }, { 'font': [] }],
                                        [{ 'size': [] }],
                                        ['bold', 'italic', 'underline', 'strike', 'blockquote'],
                                        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
                                        ['link', 'image'],
                                        ['clean']
                                    ],
                                }}
                            />

                            <TagsInput
                                ref={tagsInputRef}
                                onTagsChange={handleTagsChange}
                            />

                            <ArtistsInput
                                ref={artistsInputRef}
                                onArtistsChange={handleArtistsChange}
                            />

                        </Paper>

                    ) : (
                        <Autocomplete
                            options={eventSeriesOptions}
                            getOptionLabel={(option) => option.name}
                            value={eventSeries}
                            onChange={(e, value) => setEventSeries(value)}
                            renderInput={(params) => <TextField {...params} label="Event series" />}
                            sx={{ mb: 2 }}
                        />
                    )}
                    
                    {isEventSeatStatusesCreated && (
                        <Typography variant="body1" gutterBottom mb={2} color='warning'>
                            Event seat statuses created, you can't change location or location variant.
                        </Typography>
                    )}

                    <Autocomplete
                        required
                        options={locationOptions}
                        getOptionLabel={(option) => option.name || ''}
                        value={location}
                        inputValue={inputLocationValue}
                        onInputChange={(e, value) => {
                            setInputLocationValue(value);
                        }}
                        onChange={(e, value) => {
                            setLocation(value);
                            setInputLocationValue(value ? value.name : '');
                            fetchLocationVariants(value.id);
                            setLocationVariant(null);
                        }}
                        onClear={() => {
                            setLocation(null);
                            setInputLocationValue('');
                            setLocationVariant(null);
                        }}
                        renderInput={(params) => (
                            <TextField
                            {...params}
                            required
                            label="Location"
                        />)}
                        noOptionsText="Insert to see options"
                        sx={{ mb: 2 }}
                        disabled={isEventSeatStatusesCreated}
                    />

                    <Autocomplete
                        required
                        options={locationVariantOptions}
                        getOptionLabel={(option) => option.name || ''}
                        value={locationVariant}
                        onChange={(e, value) => setLocationVariant(value)}
                        renderInput={(params) => <TextField {...params} label="Location variant" />}
                        sx={{ mb: 2 }}
                        disabled={!location || isEventSeatStatusesCreated}
                    />

                    <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
                        <LocalizationProvider dateAdapter={AdapterDateFns} sx={{ flex: 1 }}>
                            <DateTimePicker 
                                label="Event start date"
                                value={eventDateStart}
                                onChange={setEventDateStart}
                                renderInput={(params) => <TextField {...params} />}
                            />
                        </LocalizationProvider>
                        <LocalizationProvider dateAdapter={AdapterDateFns} sx={{ flex: 1 }}>
                            <DateTimePicker 
                                label="Event end date"
                                value={eventDateEnd}
                                onChange={setEventDateEnd}
                                renderInput={(params) => <TextField {...params} />}
                            />
                        </LocalizationProvider>
                    </Box>
                    <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
                        <LocalizationProvider dateAdapter={AdapterDateFns} sx={{ flex: 1 }}>
                            <DateTimePicker
                                label="Ticket sales begin"
                                value={ticketPurchaseDateStart}
                                onChange={setTicketPurchaseDateStart}
                                renderInput={(params) => <TextField {...params} />}
                            />
                        </LocalizationProvider>
                        <LocalizationProvider dateAdapter={AdapterDateFns} sx={{ flex: 1 }}>
                            <DateTimePicker
                                label="Ticket sales end"
                                value={ticketPurchaseDateEnd}
                                onChange={setTicketPurchaseDateEnd}
                                renderInput={(params) => <TextField {...params} />}
                            />
                        </LocalizationProvider>
                    </Box>

                    <TextField
                        label="Maximum number of bookings per user"
                        type="number"
                        fullWidth
                        value={maxReservationsPerUser || 10}
                        onChange={e => setMaxReservationsPerUser(e.target.value)}
                        sx={{ mb: 2 }}
                    />

                    <FormControlLabel
                        control={<Checkbox checked={forceChoosingWithoutBreaks} onChange={e => setForceChoosingWithoutBreaks(e.target.checked)} />}
                        label="Forcing the selection of seats without breaks"
                    />

                    <Button type="submit" variant="contained" color="primary" fullWidth>
                        {eventId ? 'Edit event' : 'Create new event'}
                    </Button>
                </Box>
            </Paper>

            <Dialog open={openModal} onClose={handleCloseModal}>
                <DialogTitle>Event Created Successfully!</DialogTitle>
                <DialogContent>
                    Your event has been  {eventId ? 'edited.' : 'created.' } Would you like to go to the event page or stay here?
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseModal} color="primary">
                        Stay on this page
                    </Button>
                    <Button onClick={handleNavigateToEvent} color="secondary" variant="contained">
                        Go to Event
                    </Button>
                </DialogActions>
            </Dialog>

            <Snackbar open={snackbarOpen} autoHideDuration={6000} onClose={handleCloseSnackbar}>
                <Alert onClose={handleCloseSnackbar} severity={snackbarSeverity} sx={{ width: '100%' }}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </>
    );
};

export default CreateEventForm;
