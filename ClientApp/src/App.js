import React, { useState } from 'react';
import { EventList } from './component/event/EventList';
import { CreateEventModal } from './component/event/CreateEventModal';
import './styles.css';

function App() {
    const [refreshKey, setRefreshKey] = useState(0);
    const [isCreateModalOpen, setCreateModalOpen] = useState(false);

    const handleEventCreated = () => {
        setRefreshKey(prev => prev + 1);
        setCreateModalOpen(false);
    };

    return (
        <div className="container">
            <h1 className="title">Event Feedback Analyzer</h1>

            <EventList refreshTrigger={refreshKey} />

            <button
                className="fab-button"
                onClick={() => setCreateModalOpen(true)}
                aria-label="Create new event"
            >
                <span className="plus-icon">+</span>
            </button>

            {isCreateModalOpen && (
                <CreateEventModal
                    onClose={() => setCreateModalOpen(false)}
                    onEventCreated={handleEventCreated}
                />
            )}
        </div>
    );
}

export default App;