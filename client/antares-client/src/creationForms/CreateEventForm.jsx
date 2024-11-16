import { useState, useEffect } from 'react';
import { Box, TextField, Button, Typography, Autocomplete, Checkbox, FormControlLabel, Paper, Snackbar, Alert, CircularProgress, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { useNavigate } from 'react-router-dom';
import { request } from './../helpers/axios_helper';

const CreateEventForm = () => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [shortDescription, setShortDescription] = useState('');
    const [isPublic, setIsPublic] = useState(false);
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

    const [loading, setLoading] = useState(false); 
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');
    const [snackbarMessage, setSnackbarMessage] = useState('');

    const [openModal, setOpenModal] = useState(false);
    const [createdEventId, setCreatedEventId] = useState(null);

    const navigate = useNavigate();

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

    const fetchLocations = async (query) => {
        if (query.length < 1) return;

        try {
            const response = await request('GET', `/api/dicts/locations?query=${query}`);
            setLocationOptions(response.data);
        } catch (error) {
            console.error("Błąd podczas pobierania lokalizacji:", error);
            showSnackbar("Błąd podczas pobierania lokalizacji: " + error.message, 'error');
        }
    };

    const fetchLocationVariants = async (locationId) => {
        try {
            const response = await request('GET', `/api/dicts/location_variants?id=${locationId}`);
            setLocationVariantOptions(response.data);
        }
        catch (error) {
            console.error("Błąd podczas pobierania wariantów lokalizacji:", error);
            showSnackbar("Błąd podczas pobierania wariantów lokalizacji: " + error.message, 'error');
        }
    };

    const fetchStatuses = async () => {
        try {
            const response = await request('GET', `/api/dicts/event_statuses`);
            setEventStatusOptions(response.data);
        }
        catch (error) {
            console.error("Błąd podczas pobierania statusu:", error);
            showSnackbar("Błąd podczas pobierania statusu: " + error.message, 'error');
        }
    };

    const fetchEventSeries = async () => {
        try {
            const response = await request('GET', `/api/dicts/event_series`);
            setEventSeriesOptions(response.data);
        }
        catch (error) {
            console.error("Błąd podczas pobierania serii wydarzeń:", error);
            showSnackbar("Błąd podczas pobierania serii wydarzeń: " + error.message, 'error');
        }
    };

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            fetchLocations(inputLocationValue);
        }, 300);

        return () => clearTimeout(delayDebounceFn);
    }, [inputLocationValue]);

    
    useEffect(() => {
        setLoading(true);
        try {
            fetchStatuses();
            fetchEventSeries();
        }
        finally {
            setLoading(false);
        }

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

    const handleSubmit = (e) => {
        e.preventDefault();
        const newEvent = {
            name,
            isPublic,
            description,
            shortDescription,
            eventStatusId: eventStatus?.id,
            eventSeriesId: eventSeries?.id,
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

        request("POST", "/api/events/create", newEvent)
            // .then(() => navigate("/events"))
            .then((response) => {
                console.log("Utworzono wydarzenie, response:", response)
                setCreatedEventId(response.data.id)
                setOpenModal(true)
            })
            .catch(error => {
                console.error("Błąd przy tworzeniu wydarzenia:", error)
                showSnackbar("Błąd przy tworzeniu wydarzenia: " + error.message, 'error');
            });
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
            <Paper elevation={3} sx={{ maxWidth: 1200, mx: 'auto', mt: 4, p: 4 }}>
                <Typography variant="h4" component="h1" gutterBottom>Create new event</Typography>
                <Box component="form" onSubmit={handleSubmit}>
                    <TextField
                        label="Event name"
                        fullWidth
                        required
                        value={name}
                        onChange={e => setName(e.target.value)}
                        sx={{ mb: 2 }}
                    />

                    <TextField
                        label="Short Description"
                        fullWidth
                        multiline
                        rows={4}
                        value={shortDescription}
                        onChange={e => setShortDescription(e.target.value)}
                        sx={{ mb: 2 }}
                        htmlInput={{ maxLength: 200 }}
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
                    
                    <FormControlLabel
                        control={<Checkbox
                                    checked={isPublic}
                                    onChange={e => setIsPublic(e.target.checked)}
                        />}
                        label="Public event"
                    />

                    <Autocomplete
                        options={eventStatusOptions}
                        getOptionLabel={(option) => option.name || ''}
                        value={eventStatus}
                        onChange={(e, value) => setEventStatus(value)}
                        renderInput={(params) => (
                            <TextField
                            {...params}
                            required
                            // error={!eventStatus}
                            // helperText={!eventStatus ? 'Please select a location' : ''}
                            label="Event status"
                        />)}
                        sx={{ mb: 2 }}
                    />

                    <Autocomplete
                        options={eventSeriesOptions}
                        getOptionLabel={(option) => option.name}
                        value={eventSeries}
                        onChange={(e, value) => setEventSeries(value)}
                        renderInput={(params) => <TextField {...params} label="Event series" />}
                        sx={{ mb: 2 }}
                    />

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
                    />

                    <Autocomplete
                        required
                        options={locationVariantOptions}
                        getOptionLabel={(option) => option.name || ''}
                        value={locationVariant}
                        onChange={(e, value) => setLocationVariant(value)}
                        renderInput={(params) => <TextField {...params} label="Location variant" />}
                        sx={{ mb: 2 }}
                        disabled={!location}
                    />

                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                        <DateTimePicker 
                            label="Event start date"
                            value={eventDateStart}
                            onChange={setEventDateStart}
                            renderInput={(params) => <TextField {...params} sx={{ mb: 2 }} />}
                        />
                    </LocalizationProvider>
                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                        <DateTimePicker 
                            label="Event end date"
                            value={eventDateEnd}
                            onChange={setEventDateEnd}
                            renderInput={(params) => <TextField {...params} sx={{ mb: 2 }} />}
                        />
                    </LocalizationProvider>
                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                        <DateTimePicker
                            label="Ticket sales begin"
                            value={ticketPurchaseDateStart}
                            onChange={setTicketPurchaseDateStart}
                            renderInput={(params) => <TextField {...params} sx={{ mb: 2 }} />}
                        />
                    </LocalizationProvider>
                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                        <DateTimePicker
                            label="Ticket sales end"
                            value={ticketPurchaseDateEnd}
                            onChange={setTicketPurchaseDateEnd}
                            renderInput={(params) => <TextField {...params} sx={{ mb: 2 }} />}
                        />
                    </LocalizationProvider>

                    <TextField
                        label="Maximum number of bookings per user"
                        type="number"
                        fullWidth
                        value={maxReservationsPerUser}
                        onChange={e => setMaxReservationsPerUser(e.target.value)}
                        sx={{ mb: 2 }}
                    />

                    <FormControlLabel
                        control={<Checkbox checked={forceChoosingWithoutBreaks} onChange={e => setForceChoosingWithoutBreaks(e.target.checked)} />}
                        label="Forcing the selection of seats without breaks"
                    />

                    <Button variant="contained" component="label" sx={{ mb: 2 }}>
                        Add main photo
                        <input type="file" hidden onChange={handleImageUpload} />
                    </Button>

                    {imagePreview && (
                        <img
                            src={imagePreview}
                            alt="Image preview"
                            style={{ maxWidth: '100%', maxHeight: '400px', marginTop: '16px' }}
                        />
                    )}

                    <Button type="submit" variant="contained" color="primary" fullWidth>Create new event</Button>
                </Box>
            </Paper>

            <Dialog open={openModal} onClose={handleCloseModal}>
                <DialogTitle>Event Created Successfully!</DialogTitle>
                <DialogContent>
                    Your event has been created. Would you like to go to the event page or stay here?
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
