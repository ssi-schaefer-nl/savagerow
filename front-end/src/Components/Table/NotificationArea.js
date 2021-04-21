import React, { Component, useEffect, useState } from 'react';
import CollapsableAlert from '../CollapsableAlert/CollapsableAlert'

const NotificationArea = (props) => {
    const notifications = props.notifications
    if (notifications.length == 0) {
      return null
    }
  
    const notificationAlerts = notifications.map(notification =>
      (<CollapsableAlert severity={notification.severity} message={notification.content} onClose={() => props.handleClose(notifications.indexOf(notification))} />)
    )
  
  
  
    return (
      <div>
        {notificationAlerts}
      </div>
    )
  }

  export default NotificationArea