# ChatCo

### Introduction

ChatCo introduces private messaging and ignore functionalities, allowing players to communicate privately and manage who can interact with them.

### Features

- **Private Messaging**: Send private messages to other players using `/msg`, `/pm`, or `/w`.
- **Ignore Functionality**: Ignore or unignore players to prevent receiving their chat and private messages.
- **Ignore List Management**: View the list of players you are currently ignoring with `/ignorelist`.

### Installation

1. **Download the Plugin**: Obtain the latest version of ChatCo and place the `.jar` file into your server's `plugins` directory.
2. **Restart the Server**: Restart your PaperMC server to load the plugin.
3. **Verify Installation**: Use `/plugins` in-game to ensure ChatCo is listed and active.

### Configuration

No additional configuration is required out of the box. All features are ready to use upon installation. Future updates may include configurable options.

### Commands and Permissions

#### Commands

- `/msg <player> <message>`: Send a private message to a player.
    - **Aliases**: `/pm`, `/w`
- `/ignore <player>`: Ignore or unignore a player.
- `/ignorelist`: Display the list of players you are currently ignoring.

#### Permissions

By default, all players have access to ChatCo commands. To restrict commands, use the following permission nodes:

- `chatco.msg`: Allows use of `/msg`, `/pm`, and `/w`.
- `chatco.ignore`: Allows use of `/ignore`.
- `chatco.ignorelist`: Allows use of `/ignorelist`.

*Note: If no permissions plugin is installed, all players can use the commands.*

### Usage Examples

#### Sending a Private Message

To send a private message to another player:

`/msg Steve Hello there!`

- **Sender Sees**:  
  *You whisper to Steve: Hello there!*
- **Recipient Sees**:  
  *Alex whispers: Hello there!*

#### Ignoring a Player

To ignore a player named Steve:

`/ignore Steve`

- **Response**:  
  *You have ignored Steve.*

#### Unignoring a Player

To unignore (toggle) a player named Steve:

`/ignore Steve`

- **Response**:  
  *You have unignored Steve.*

#### Viewing Ignored Players

To view your ignore list:

`/ignorelist`

- **Response if ignoring players**:  
  *Players you are ignoring:*  
  *Steve, Alex*
- **Response if not ignoring anyone**:  
  *You are not ignoring any players.*