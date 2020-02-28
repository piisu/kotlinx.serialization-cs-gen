tasks.create("run", Exec::class) {
    commandLine = listOf("dotnet", "run", "--project","CborTest/CborTest.csproj")
}

tasks.create("build", Exec::class) {
    commandLine = listOf("dotnet", "build", "CborTest.sln")
}