self.addEventListener('push', function(e) {
    const message = e.data.text(); 
  
    // You can add options like an icon
    
    const options = {
        body: message 
    };

    e.waitUntil(
        self.registration.showNotification('Title', options)
    );
});