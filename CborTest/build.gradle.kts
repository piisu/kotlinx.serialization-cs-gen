


tasks {
    create("build", Exec::class) {
        commandLine = listOf("dotnet", "build", "CborTest.sln")
    }
    create("run", Exec::class) {
        commandLine = listOf("dotnet", "run", "--project","CborTest/CborTest.csproj")
    }
}