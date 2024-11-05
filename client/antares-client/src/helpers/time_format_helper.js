
const getDateFormArray = (eventDate) => {
    return new Date(eventDate[0], eventDate[1] - 1, eventDate[2], eventDate[3], eventDate[4]);
}

export const formatDate = (dateEvent) => {
    const date = getDateFormArray(dateEvent);

    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}.${month}.${year}`;
}

export const formatTime = (dateEvent) => {
    const date = getDateFormArray(dateEvent);

    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
}

export const formatDateTime = (dateEvent) => {
    return `${formatDate(dateEvent)} ${formatTime(dateEvent)}`;
}