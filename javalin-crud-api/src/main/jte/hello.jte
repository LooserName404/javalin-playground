@param app.HelloPage page
@param app.Localizer localizer

<html lang="en">
<body>
    <p>${localizer.localize("hello.visitor")}</p>
    <p>${localizer.localize("hello.user-of-the-day", page.userName,page.userKarma)}</p>
</body>
</html>