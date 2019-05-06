# Add internal certificate to Arch Linux

### Step 1 - Download certificate

Download certificate from browser, Please refer browser manual to extract certificate. Use Base 64 - X.509 option while extracting certificate. By default the certificate file will have .cer extension. <br>

Example output file : xyz.cer


### Step 2 - Rename certificate to 

Rename xyz.cer to xyz.crt

### Step 3 - Copy certificate to machine

Copy the crt file to ```/etc/ca-certificates/trust-source/anchors/``` folder of the machine.

### Step 4 - Extract certificate

execute ```trust extract-compat```

### Step 5 - Update certificate

execute ```update-ca-trust```

### Step 6 - Validate

Navigate to ```/etc/ssl/certs``` folder to verify the certificate added exists

