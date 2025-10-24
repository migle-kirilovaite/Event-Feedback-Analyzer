import React, { useState } from 'react';
import { eventApi } from '../../services/api';

export const CreateEventModal = ({ onClose, onEventCreated }) => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const TITLE_MAX_LENGTH = 64;
    const DESCRIPTION_MAX_LENGTH = 256;

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!title.trim()) {
            setError('Title must not be blank');
            return;
        }

        setIsLoading(true);
        setError(null);

        try {
            await eventApi.createEvent({ title, description });
            if (onEventCreated) {
                onEventCreated();
            }
        } catch (err) {
            setError('Failed to create event');
            console.error('Error creating event:', err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleTitleChange = (e) => {
        const value = e.target.value;
        if (value.length <= TITLE_MAX_LENGTH) {
            setTitle(value);
        }
    };

    const handleDescriptionChange = (e) => {
        const value = e.target.value;
        if (value.length <= DESCRIPTION_MAX_LENGTH) {
            setDescription(value);
        }
    };

    return (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && onClose()}>
            <div className="modal-content">
                <div className="modal-header">
                    <h3>Create New Event</h3>
                    <button onClick={onClose} className="modal-close">&times;</button>
                </div>
                {error && <div className="error">{error}</div>}
                <form onSubmit={handleSubmit} className="modal-form">
                    <div className="form-group">
                        <label className="label">
                            Title
                            <span className="char-count">
                                {title.length}/{TITLE_MAX_LENGTH}
                            </span>
                        </label>
                        <input
                            type="text"
                            value={title}
                            onChange={handleTitleChange}
                            className="modal-input"
                            required
                            maxLength={TITLE_MAX_LENGTH}
                            autoFocus
                        />
                    </div>
                    <div className="form-group">
                        <label className="label">
                            Description
                            <span className="char-count">
                                {description.length}/{DESCRIPTION_MAX_LENGTH}
                            </span>
                        </label>
                        <textarea
                            value={description}
                            onChange={handleDescriptionChange}
                            className="modal-textarea"
                            maxLength={DESCRIPTION_MAX_LENGTH}
                        />
                    </div>
                    <div className="modal-actions">
                        <button type="button" onClick={onClose} className="button secondary">
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={isLoading || !title.trim()}
                            className="button"
                        >
                            {isLoading ? 'Creating...' : 'Create Event'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};