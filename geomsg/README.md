# Geomsg

Geolocated audio/video messages.

This demo is designed as a base on which you can build web apps for geolocated messaging.

It contains:

* Sets page background to a Google map for the current location.
* Button for recording a new message.
* List of recorded messages.

It does not:

* Persist any data except the last recorded GPS coordinates, stored in localStorage.
* Send any data anywhere.

You could:

* ↔️ Add support for p2p messaging with Web-RTC 
* 🌏 Only show messages within a geo-boundary 
* 🗣 Add support for accounts with social media services 
* 👻 Make a Sherlock Holmes geolocated mystery story 
* ⭕️ Make an audio/video version of 紀念章, the rubber stamps for collectors 

It was tested on:

* Firefox on iOS, to test recording of video with audio through file inputs.
* That's it for now. Android should be ok since we can actually use getUserMedia.
* Desktop browsers, at least on Mac, didn't appear to support webcam/mic input through file input fields.
