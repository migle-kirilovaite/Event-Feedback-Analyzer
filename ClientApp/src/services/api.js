import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    }
});

export const eventApi = {
    createEvent: async (request) => {
        const response = await api.post('/events', request);
        return response.data;
    },

    listEvents: async () => {
        const response = await api.get('/events');
        return response.data;
    },

    submitFeedback: async (eventId, request) => {
        const response = await api.post(`/events/${eventId}/feedback`, request);
        return response.data;
    },

    getEventSummary: async (eventId) => {
        const response = await api.get(`/events/${eventId}/summary`);
        return response.data;
    },
};