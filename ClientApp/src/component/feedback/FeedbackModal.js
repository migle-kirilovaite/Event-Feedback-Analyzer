import React, { useState } from 'react';
import { eventApi } from '../../services/api';

export const FeedbackModal = ({ eventId, onClose, onFeedbackSubmitted }) => {
    const [text, setText] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const FEEDBACK_MAX_LENGTH = 1000;

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!text.trim()) {
            setError('Feedback text must not be blank');
            return;
        }

        setIsLoading(true);
        setError(null);

        try {
            await eventApi.submitFeedback(eventId, { text });
            setText('');
            if (onFeedbackSubmitted) {
                onFeedbackSubmitted();
            }
            onClose();
        } catch (err) {
            setError('Failed to submit feedback');
            console.error('Error submitting feedback:', err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleTextChange = (e) => {
        const value = e.target.value;
        if (value.length <= FEEDBACK_MAX_LENGTH) {
            setText(value);
        }
    };

    return (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && onClose()}>
            <div className="modal-content">
                <div className="modal-header">
                    <h3>Write Feedback</h3>
                    <button onClick={onClose} className="modal-close">&times;</button>
                </div>
                {error && <div className="error">{error}</div>}
                <form onSubmit={handleSubmit} className="modal-form">
                    <div className="form-group">
                        <label className="label">
                            Feedback
                            <span className="char-count">
                                {text.length}/{FEEDBACK_MAX_LENGTH}
                            </span>
                        </label>
                        <textarea
                            value={text}
                            onChange={handleTextChange}
                            className="modal-textarea"
                            required
                            maxLength={FEEDBACK_MAX_LENGTH}
                            placeholder="Write your feedback here..."
                            autoFocus
                        />
                    </div>
                    <div className="modal-actions">
                        <button type="button" onClick={onClose} className="button secondary">
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={isLoading || !text.trim()}
                            className="button"
                        >
                            {isLoading ? 'Submitting...' : 'Submit Feedback'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};