# Savestate


## 2026-10-24: Rolling release ZIP publisher and recovery policy

- Added `.github/actions/publish-release-zip/action.yml` for ZIP publication, rebuild metadata, SHA-256 inventory, and rolling three-release retention.
- Added `.github/RELEASE_ZIP_POLICY.md` describing the required publication pattern.
- Existing workflows are not yet migrated in this commit. Verification of actual publication/retention is pending a successful build.

## 2026-10-24: Migrate build output to rolling release ZIPs

- Updated `.github/workflows/build.yml` to publish the produced JAR as a ZIP GitHub Release asset using `.github/actions/publish-release-zip` instead of storing it as a long-lived Actions artifact.
- Added explicit `contents: write` workflow permission for release publication.
- Build commands and source packaging steps were left unchanged; publication runs only after a successful non-PR build and only when a JAR exists.
- The publisher embeds commit/run recovery metadata and file SHA-256 values, and retains the newest three successful CI releases.
- Verification pending a successful Actions run; this tool session did not dispatch the workflow.

## 2026-10-24: Fix release publisher step indentation

- Corrected the indentation of the new local release-ZIP publisher step in `.github/workflows/build.yml` so it remains under the build job's `steps` list.
- Build and packaging commands were not changed. YAML/runtime verification is pending a successful Actions run.

## 2026-10-24: Enforce release ZIP policy for future build workflows

- Added `.github/workflows/release-zip-policy-check.yml`, a read-only guard that checks future build workflows use the local release ZIP publisher and all temporary Actions artifact uploads set one-day retention.
- The guard does not publish or delete artifacts. It will run automatically on the next push/PR; runtime verification is pending.

## 2026-10-24: Repair pre-existing CI blocker discovered during release ZIP migration

- Removed the duplicate Java distribution key that made the workflow invalid before any build job could start.
- This was required for the new release ZIP publisher to be reachable; no build command or artifact path was changed.
- The policy guard and release ZIP action remain in place. Verification pending the next Actions run.

## 2026-10-24: Enforce release ZIP-only build delivery

- Updated the local publisher so intermediate handoffs live in draft `ci-*` releases and only a successful final ZIP publishes the release. Rolling retention counts only published successful CI releases and keeps the newest three.
- Updated `.github/workflows/release-zip-policy-check.yml` to reject all `actions/upload-artifact` use and require the local release ZIP publisher for build outputs.
- Draft handoffs are cleaned by the account-level cleanup workflow if a build never reaches final publication. Verification pending workflow runs.
