// Set up proxy server URL
const proxyUrl = "http://localhost:8080/proxy";

// Set up remote server URL
const remoteUrl = "https://example.com/api/data";

// Set up AJAX request using jQuery
$.ajax({
  url: proxyUrl,
  type: "GET",
  dataType: "json",
  data: {
    url: remoteUrl
  },
  success: function(data, textStatus, xhr) {
    // Handle success
    console.log(data);
  },
  error: function(xhr, textStatus, errorThrown) {
    // Handle error
    console.error(errorThrown);
  }
});
