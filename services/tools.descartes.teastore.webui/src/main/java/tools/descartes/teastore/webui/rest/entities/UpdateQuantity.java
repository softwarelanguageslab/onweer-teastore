package tools.descartes.teastore.webui.rest.entities;

import java.util.Objects;

public class UpdateQuantity {
    private long pid; // product id
    private int quantity;

    public UpdateQuantity() {
    }

    public UpdateQuantity(UpdateQuantity uq) {
        this.setPid(uq.getPid());
        this.setQuantity(uq.getQuantity());
    }

    public long getPid() {
        return this.pid;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  UpdateQuantity)) {
            return false;
        }
        UpdateQuantity uq = (UpdateQuantity) obj;
        return this.getPid() == uq.getPid() && this.getQuantity() == uq.getQuantity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, quantity);
    }
}
