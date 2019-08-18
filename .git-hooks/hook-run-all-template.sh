#!/bin/sh

hook_name=$(basename $0)
hook_dir=".git-hooks/$hook_name"

if [[ -d $hook_dir ]]; then
  stdin=$(cat /dev/stdin)

  for hook in $hook_dir/*; do
    echo "Running $hook_name/$hook hook"
    echo "$stdin" | $hook "$@"

    exit_code=$?

    if [ $exit_code != 0 ]; then
      exit $exit_code
    fi
  done
fi

exit 0
