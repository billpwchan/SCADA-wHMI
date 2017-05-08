Set objArgs = WScript.Arguments

If objArgs.Count > 1 Then

'Create a WshShell Object
Set WshShell = Wscript.CreateObject("Wscript.Shell")

'Create a WshShortcut Object
Set oShellLink = WshShell.CreateShortcut(objArgs(0))

'Set the Target Path for the shortcut
oShellLink.TargetPath = objArgs(1)

'Set the additional parameters for the shortcut
oShellLink.Arguments = ""

'Save the shortcut
oShellLink.Save

'Clean up the WshShortcut Object
Set oShellLink = Nothing 

Else

WScript.Echo "Arguments incorrects :"
WScript.Echo "cscript //nologo shortcut.vbs <path_raccourci.lnk> <path_objet_pointe>"

End If