<#import "/spring.ftl" as spring/>

<!DOCTYPE html>
<html lang="en">
<head>
    <#include "head.ftl">

    <!-- inject:css -->
    <!-- endinject -->
</head>
<body>

<sd-app>Loading...</sd-app>

<script>
    // Fixes undefined module function in SystemJS bundle
    function module() {}
</script>

<!-- shims:js -->
<!-- endinject -->

<% if (BUILD_TYPE === 'dev') { %>
<script src="<%= APP_BASE %>app/system-config.js"></script>
<% } %>

<!-- libs:js -->
<!-- endinject -->

<!-- inject:js -->
<!-- endinject -->

<% if (BUILD_TYPE === 'dev') { %>
<script>
    System.import('<%= BOOTSTRAP_MODULE %>')
        .catch(function (e) {
            console.error(e.stack || e, 'Error');
        });
</script>
<% } %>

<#include "version.ftl">
</body>
</html>
