#!/bin/bash

echo "Stopping servers..."
gfsh <<!

connect --locator=localhost[10334]
shutdown --include-locators=false
Y

exit;
!
