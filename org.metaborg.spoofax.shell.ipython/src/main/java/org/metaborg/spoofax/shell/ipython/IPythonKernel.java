package org.metaborg.spoofax.shell.ipython;

import java.io.IOException;

import org.metaborg.core.completion.ICompletionService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.shell.client.IDisplay;
import org.metaborg.spoofax.shell.client.IEditor;
import org.metaborg.spoofax.shell.client.IInputHistory;
import org.metaborg.spoofax.shell.output.StyledText;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public class IPythonKernel implements IDisplay, IEditor {

    @Override
    public String getInput() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSpoofaxCompletion(ICompletionService<ISpoofaxParseUnit> completionService) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IInputHistory history() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPrompt(StyledText promptString) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setContinuationPrompt(StyledText styledText) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void displayResult(StyledText s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void displayError(StyledText s) {
        // TODO Auto-generated method stub
        
    }

}
