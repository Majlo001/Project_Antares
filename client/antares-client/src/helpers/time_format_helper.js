
export const getFullDateFormArray = (eventDate) => {
    return new Date(eventDate[0], eventDate[1] - 1, eventDate[2], eventDate[3], eventDate[4]);
}

export const getDateFromArray = (eventDate) => {
    return new Date(eventDate[0], eventDate[1] - 1, eventDate[2]);
}

export const formatDate = (dateEvent) => {
    const date = getFullDateFormArray(dateEvent);

    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}.${month}.${year}`;
}

export const formatTime = (dateEvent) => {
    const date = getFullDateFormArray(dateEvent);

    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
}

export const formatDateTime = (dateEvent) => {
    return `${formatDate(dateEvent)} ${formatTime(dateEvent)}`;
}

export const formatDateToLocal = (date) => {
    const offset = date.getTimezoneOffset();
    const localDate = new Date(date.getTime() - offset * 60 * 1000);
    return localDate.toISOString().split("T")[0];
};