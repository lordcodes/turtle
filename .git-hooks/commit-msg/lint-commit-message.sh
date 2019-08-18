#!/bin/sh

# Lints commit messages to ensure they follow rules for good, clear commit messages.
# https://chris.beams.io/posts/git-commit/
#
# - Separate subject from body with a blank line
# - Limit the subject line to 72 characters
# - Capitalize the subject line
# - Do not end the subject line with a period
# - Wrap the body at 72 characters
#
# Added some additional checks on top of them as well.
# - Empty commit message

was_error=false

error() {
    echo "$1"
    was_error=true
}

remove_jira_prefix() {
  local jira_id_regex="[A-Z0-9]{1,10}-?[A-Z0-9]+-\d+"
  local commit_message=$1
  local commit_jira_id=$(echo "$commit_message" | grep -Eo -m 1 "$jira_id_regex" | head -1)
  local prefix="$commit_jira_id: "
  local result=${commit_message#"$prefix"}
  echo "$result"
}

commit_file=$1
commit_message=$(cat $1)
commit_message=$(remove_jira_prefix "$commit_message")

# Check commit message isn't empty or contains only whitespace
trimmed_commit=$(echo $commit_message | tr -d "[:space:]")
if [[ ${#trimmed_commit} -lt 1 ]]; then
  echo "Empty commit message"
  exit 1
fi

line_number=0
while read -r line; do
    # Ignore comment lines (don't count line number either)
    [[ "$line" =~ ^\#.* ]] && continue

    line_number+=1
    length=${#line}

    # Subject line tests
    if [[ $line_number -eq 1 ]]; then
        [[ $length -gt 72 ]] && error "Limit the subject line to 72 characters"
        [[ ! "$line" =~ ^[A-Z].*$ ]] && error "Capitalise the subject line"
        [[ "$line" == *. ]] && error "Do not end the subject line with a period"
    fi

    # Rules related to the commit message body
    [[ $line_number -eq 2 ]] && [[ -n $line ]] && error "Separate subject from body with a blank line"
    [[ $line_number -gt 1 ]] && [[ $length -gt 72 ]] && error "Wrap the body at 72 characters"
done <<< "$commit_message"

if [[ "$was_error" = true ]]; then
  echo  "Commit message rejected due to containing invalid formatting."
  exit 1
fi

exit 0
