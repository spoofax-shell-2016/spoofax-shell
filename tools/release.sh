#!/usr/bin/env bash

tagRelease() {
    printf "Tagging new release...\n"

    git checkout master && \
    git tag -a "v$VERSION" -m "Release version $VERSION" && \
    git push origin "v$VERSION"
}

generateSite() {
    printf "Generating site...\n"

    ./tools/site-deploy.sh
}

tagSite() {
    printf "Changing directory in order to tag the site...\n"
    pushd ../spoofax-shell.github.io

    git checkout master && \
    git pull master && \
    git tag -a "v$VERSION" -m "Site belonging to $VERSION" && \
    git push origin "v$VERSION"

    popd
}

addJarsToGitHub() {
    printf "Creating GitHub release...\n"

    # Create a new release on GitHub
    local JSON=$(printf '{"tag_name": "v%s",
    			  "target_commitish": "master",
			  "name": "v%s",
			  "body": "Release of version %s",
			  "draft": false,
			  "prerelease": false}' "$VERSION" "$VERSION" "$VERSION")
    curl --data "$JSON" https://api.github.com/repos/spoofax-shell/spoofax-shell/releases?ACCESS_TOKEN="$ACCESS_TOKEN"

    # TODO: extract ID from response ^
    #ID=

    # Add the JARS to the release
    #JARS=("org.metaborg.spoofax.shell.console/target/org.metaborg.spoofax.shell.console-0.0.1-SNAPSHOT-jar-with-dependencies.jar")
    #for $JAR in $JARS; do
    #    curl -H "Authorization: token \"$ACCESS_TOKEN\"" \
    #         -H "Content-Type: application/java-archive" \
    #        --data-binary @"$JAR" \
    #        https://uploads.github.com/repos/spoofax-shell/spoofax-shell/releases/$ID/assets?name="$(basename "$JAR")"
    #done
}

addToTrello() {
    printf "Adding Trello cards is as of yet not implemented\n"
}

increaseVersion() {
    printf "Increasing the version numbers...\n"

    mvn --batch-mode release:update-versions -DautoVersionSubmodules=true

    git add -u
    git commit -m "Post release version bump"
    git push origin master
}

if [[ "$#" -ne 1 ]]; then
    printf "Usage: %s GITHUB_TOKEN\n" "$0" >&2
    exit 1
elif [[ ! -e .git ]]; then
    printf "This script needs to be run from the top-level directory\n" >&2
    exit 1
fi

TOKEN="$1"
#VERSION=$(grep --max-count=1 "<version>" pom.xml | awk -F ">|<" "{print $3}")
VERSION=$(grep --max-count=1 -oP '<version>\K[^<]+' pom.xml)

printf "GitHub token %s\nFound version: %s\n" "$TOKEN" "$VERSION"

tagRelease
generateSite
tagSite
addJarsToGitHub
addToTrello
increaseVersion

