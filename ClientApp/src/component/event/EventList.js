import React, { useState, useEffect, useRef } from 'react';
import { eventApi } from '../../services/api';
import { FeedbackModal } from '../feedback/FeedbackModal';
import { SentimentSummary } from '../summary/SentimentSummary';

export const EventList = ({ refreshTrigger }) => {
    const [events, setEvents] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedEventId, setSelectedEventId] = useState(null);
    const sentimentRefs = useRef({});

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                setIsLoading(true);
                const data = await eventApi.listEvents();
                setEvents(data);
                setError(null);
            } catch (err) {
                setError('Failed to load events');
                console.error('Error fetching events:', err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchEvents();
    }, [refreshTrigger]);

    const handleFeedbackSubmitted = (eventId) => {
        if (sentimentRefs.current[eventId]) {
            sentimentRefs.current[eventId].fetchSummary();
        }
    };

    if (isLoading) return <div className="loading">Loading events...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <>
            <div className="events-grid">
                {events.map((event) => (
                    <div key={event.id} className="event-card">
                        <div className="event-header">
                            <h3 className="event-title">{event.title}</h3>
                        </div>

                        <div className="event-content">
                            <p className="event-description">{event.description}</p>

                            <SentimentSummary
                                eventId={event.id}
                                ref={el => sentimentRefs.current[event.id] = el}
                            />

                            <button
                                className="write-feedback-button"
                                onClick={() => setSelectedEventId(event.id)}
                            >
                                Write Feedback
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            {selectedEventId && (
                <FeedbackModal
                    eventId={selectedEventId}
                    onClose={() => setSelectedEventId(null)}
                    onFeedbackSubmitted={() => handleFeedbackSubmitted(selectedEventId)}
                />
            )}
        </>
    );
};