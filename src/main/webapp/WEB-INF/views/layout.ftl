<#import "/spring.ftl" as spring/>

<!DOCTYPE html>
<html lang="en">
<head>
    <base href="${baseUrl}">

    <link rel="shortcut icon" href="<@spring.url "/assets/img/faviconfi.png"/>" type="image/x-icon"/>
    <title>FaithInquest</title>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="FaithInquest">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=0">

    <link rel="stylesheet" href="css/main.css">

    <script type="application/javascript">
        window.AppConfig = {
            appPath: "<@spring.url "/"/>",
            baseUrl: "${baseUrl}/",
            addThisKey: "${addThisKey}",
            crazyEggKey: "00527990",
            optimizelyKey: "6512121647"
        };
    </script>

</head>
<body>

<sd-app>Loading...</sd-app>

<script>
    // Fixes undefined module function in SystemJS bundle
    function module() {}
</script>

<#if buildEnv?? && buildEnv == 'prod'>
<#--PROD-->

<!-- inject:js -->
<script src="js/shims.js"></script>
<script src="js/app.js"></script>
<!-- endinject -->

<#--end-->
<#else >
<#--DEV-->

<!-- shims:js -->
<script src="js/core-js/client/shim.min.js"></script>
<script src="js/systemjs/dist/system.src.js"></script>
<!-- endinject -->

<script src="app/system-config.js"></script>

<!-- libs:js -->
<script src="js/zone.js/dist/zone.js"></script>
<script src="js/.tmp/Rx.min.js"></script>
<!-- endinject -->

<script>
    System.import('app/main').catch(function (e) {
        console.error(e, 'Error');
    });
</script>

<#--end-->
</#if>

<#include "version.ftl">
</body>
</html>
