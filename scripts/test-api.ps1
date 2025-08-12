param(
  [string]$BaseUrl = "http://localhost:8080"
)

function Fail($msg) {
  Write-Host "FAIL: $msg" -ForegroundColor Red
  exit 1
}

function Assert($cond, $msg) {
  if (-not $cond) { Fail $msg }
}

# Warten bis Server erreichbar ist
$healthUrl = "$BaseUrl/api/todos"
$maxAttempts = 30
for ($i = 0; $i -lt $maxAttempts; $i++) {
  try {
    Invoke-RestMethod -Method Get -Uri $healthUrl -TimeoutSec 2 | Out-Null
    break
  } catch {
    Start-Sleep -Seconds 1
  }
  if ($i -eq ($maxAttempts - 1)) { Fail "Server nicht erreichbar: $BaseUrl" }
}

# Create
$createBody = @{ title = "First from script"; completed = $false }
$created = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/todos" -Body ($createBody | ConvertTo-Json) -ContentType 'application/json'
Assert ($null -ne $created.id) "Create: keine id erhalten"
$id = $created.id

# List
$list = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/todos"
if ($list -isnot [System.Array]) { $list = @($list) }
Assert ($list.Length -ge 1) "List: leer"

# Get
$single = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/todos/$id"
Assert ($single.title -eq "First from script") "Get: unerwarteter title"

# Update
$updateBody = @{ title = "Updated via script"; completed = $true }
$updated = Invoke-RestMethod -Method Put -Uri "$BaseUrl/api/todos/$id" -Body ($updateBody | ConvertTo-Json) -ContentType 'application/json'
Assert ($updated.title -eq "Updated via script") "Update: title nicht aktualisiert"
Assert ($updated.completed -eq $true) "Update: completed nicht aktualisiert"

# Delete
Invoke-RestMethod -Method Delete -Uri "$BaseUrl/api/todos/$id"

# Verify 404
$got404 = $false
try {
  Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/todos/$id" | Out-Null
} catch {
  try {
    $statusCode = $_.Exception.Response.StatusCode.value__
  } catch {
    $statusCode = -1
  }
  if ($statusCode -eq 404) { $got404 = $true }
}
Assert $got404 "Nach Delete wird kein 404 geliefert"

Write-Host "API Tests erfolgreich" -ForegroundColor Green
exit 0


