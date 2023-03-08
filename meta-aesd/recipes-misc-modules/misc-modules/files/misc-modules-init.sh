#!/bin/sh
# Init scull script for the misc-modules module

MODULE_HELLO="hello"
MODULE_FAULTY="faulty"

#if [ "$(lsmod | grep $MODULE)" ]; then
#    echo "Unloading $MODULE module..."
#    rmmod $MODULE
#else
#    echo "Loading $MODULE module..."
#    modprobe $MODULE
#fi

case "$1" in
  start)
     lsmod
     echo "Loading $MODULE_HELLO module..."
     #load_device
     modprobe $MODULE_HELLO
     if [ "$(lsmod | grep $MODULE_HELLO)" ]; then
        echo "Module $MODULE_HELLO loaded successfully."
     else
        echo "Failed to load module $MODULE_HELLO."
        #exit 1
     fi

     echo "Loading $MODULE_FAULTY module..."
     /usr/bin/module_load $MODULE_FAULTY
     if [ "$(lsmod | grep $MODULE_FAULTY)" ]; then
        echo "Module $MODULE_FAULTY loaded successfully."
     else
        echo "Failed to load module $MODULE_FAULTY."
        #exit 1
     fi
     lsmod
     ;;
  stop)
     lsmod
     echo "Unloading $MODULE_HELLO module..."
     #unload_device
     rmmod $MODULE_HELLO
     if ! [ "$(lsmod | grep $MODULE_HELLO)" ]; then
        echo "Module $MODULE_HELLO unloaded successfully."
     else
        echo "Failed to unload module $MODULE_HELLO."
        #exit 1
     fi

     echo "Unloading $MODULE_FAULTY module..."
     /usr/bin/module_unload $MODULE_FAULTY
     if ! [ "$(lsmod | grep $MODULE_FAULTY)" ]; then
        echo "Module $MODULE_FAULTY unloaded successfully."
     else
        echo "Failed to unload module $MODULE_FAULTY."
        #exit 1
     fi
     lsmod
     ;;
  force-reload|restart)
     lsmod
     echo "Reloading $MODULE_HELLO module..."
     #unload_device
     #load_device
     if rmmod $MODULE_HELLO && modprobe $MODULE_HELLO ; then
        echo "Module $MODULE_HELLO reloaded successfully."
     else
        echo "Failed to reload module $MODULE_HELLO."
        #exit 1
     fi

     echo "Reloading $MODULE_FAULTY module..."
     #unload_device
     #load_device
     if /usr/bin/module_unload $MODULE_FAULTY && /usr/bin/module_load $MODULE_FAULTY ; then
        echo "Module $MODULE_FAULTY reloaded successfully."
     else
        echo "Failed to reload module $MODULE_FAULTY."
        #exit 1
     fi
     lsmod
     ;;
  *)
     echo "Usage: $0 {start|stop|restart|force-reload}"
     exit 1
esac

exit 0