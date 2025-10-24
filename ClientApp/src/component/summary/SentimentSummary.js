import React, { useState, useEffect, forwardRef, useImperativeHandle, useCallback } from 'react';
import { eventApi } from '../../services/api';

export const SentimentSummary = forwardRef(({ eventId }, ref) => {
    const [summary, setSummary] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchSummary = useCallback(async () => {
        try {
            setIsLoading(true);
            setError(null);
            const data = await eventApi.getEventSummary(eventId);
            setSummary(data);
        } catch (err) {
            setError('Failed to load sentiment summary');
            console.error('Error fetching summary:', err);
        } finally {
            setIsLoading(false);
        }
    }, [eventId]);

    useEffect(() => {
        fetchSummary();
    }, [fetchSummary]);

    useImperativeHandle(ref, () => ({
        fetchSummary
    }));

    if (error) return (
        <div className="sentiment-summary">
            <div className="summary-header">
                <h3>Feedback Summary</h3>
                <button onClick={fetchSummary} className="refresh-button">
                    <i className="fas fa-sync-alt"></i>
                </button>
            </div>
            <div className="error">{error}</div>
        </div>
    );

    return (
        <div className="sentiment-summary">
            <div className="summary-header">
                <h3>Feedback Summary</h3>
                <button
                    onClick={fetchSummary}
                    className={`refresh-button ${isLoading ? 'spinning' : ''}`}
                    disabled={isLoading}
                >
                    <i className="fas fa-sync-alt"></i>
                </button>
            </div>
            {isLoading ? (
                <div className="loading">Loading summary...</div>
            ) : summary && (
                <div className="summary-content">
                    <div className="total-feedback">
                        Total Feedback: {summary.totalFeedback}
                    </div>
                    <div className="sentiment-stats">
                        <div className="sentiment-stat positive">
                            <i className="fas fa-smile"></i>
                            <span className="count">{summary.positive.count}</span>
                            <span className="score">
                                {summary.positive.avgScore?.toFixed(2) || '0.00'}
                            </span>
                        </div>
                        <div className="sentiment-stat neutral">
                            <i className="fas fa-meh"></i>
                            <span className="count">{summary.neutral.count}</span>
                            <span className="score">
                                {summary.neutral.avgScore?.toFixed(2) || '0.00'}
                            </span>
                        </div>
                        <div className="sentiment-stat negative">
                            <i className="fas fa-frown"></i>
                            <span className="count">{summary.negative.count}</span>
                            <span className="score">
                                {summary.negative.avgScore?.toFixed(2) || '0.00'}
                            </span>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
});