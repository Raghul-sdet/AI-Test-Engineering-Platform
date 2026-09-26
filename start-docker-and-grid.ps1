$dockerPath = "C:\Program Files\Docker\Docker\Docker Desktop.exe"

Write-Host "Checking if Docker is responsive..."
$dockerResponsive = $false
try {
    docker info > $null 2>&1
    if ($LASTEXITCODE -eq 0) {
        $dockerResponsive = $true
    }
} catch {
}

if (-not $dockerResponsive) {
    Write-Host "Docker is not running. Launching Docker Desktop..."
    if (Test-Path $dockerPath) {
        Start-Process $dockerPath
    } else {
        Write-Error "Docker Desktop executable not found at $dockerPath"
        exit 1
    }

    $timeout = 180
    $elapsed = 0
    while (-not $dockerResponsive -and $elapsed -lt $timeout) {
        Start-Sleep -Seconds 5
        $elapsed += 5
        try {
            docker info > $null 2>&1
            if ($LASTEXITCODE -eq 0) {
                $dockerResponsive = $true
                Write-Host "Docker is now responsive."
            }
        } catch {
        }
    }

    if (-not $dockerResponsive) {
        Write-Error "Timeout reached while waiting for Docker to become responsive."
        exit 1
    }
} else {
    Write-Host "Docker is already responsive."
}

Write-Host "Starting Docker Compose grid..."
docker compose up -d

Write-Host "Waiting for grid containers to be fully up..."
$gridReady = $false
$timeout = 120
$elapsed = 0

while (-not $gridReady -and $elapsed -lt $timeout) {
    Start-Sleep -Seconds 5
    $elapsed += 5
    
    # Get status of containers
    $statusOutput = docker compose ps --format "{{.State}}"
    if ([string]::IsNullOrWhiteSpace($statusOutput)) {
        continue
    }
    
    if ($statusOutput -match "(?i)restarting|exited|created|starting|dead|paused") {
        # still waiting
    } else {
        $gridReady = $true
    }
}

if (-not $gridReady) {
    Write-Error "Timeout reached while waiting for grid containers to start. Current status:"
    docker compose ps
    exit 1
}

Write-Host "Docker and grid are successfully up and running!"
exit 0
